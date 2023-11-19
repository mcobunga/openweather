package com.bonface.openweather.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.data.model.SearchResult
import com.bonface.openweather.databinding.ItemSearchResultBinding
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchAdapter(
    private val token: AutocompleteSessionToken,
    private val client: PlacesClient
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(), Filterable {

    private val locationSuggestions: ArrayList<SearchResult> = ArrayList()
    private val locationSuggestionIds: ArrayList<String> = ArrayList()

    inner class ViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldList: SearchResult, newList: SearchResult): Boolean {
            return oldList.secondaryText == newList.secondaryText
        }
        override fun areContentsTheSame(oldList: SearchResult, newList: SearchResult): Boolean {
            return oldList == newList
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResult = differ.currentList[position]
        holder.binding.apply {
            location.text = searchResult.primaryText
            address.text = searchResult.secondaryText
            root.setOnClickListener {
                onItemClicked?.let { it(searchResult) }
            }
        }
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList: ArrayList<SearchResult>? = ArrayList()
            if (constraint?.isNotEmpty() == true) {
                filteredList = searchLocation(constraint)
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            if (filterResults != null) {
                differ.submitList(filterResults.values as MutableList<SearchResult>)
                Timber.e("Published locations: ${filterResults.values}")
                notifyDataSetChanged()
            }
        }
    }

    private fun searchLocation(query: CharSequence): ArrayList<SearchResult>? {
        val autocompletePredictionsRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery(query.toString())
            .setSessionToken(token)
        val searchResult = client.findAutocompletePredictions(autocompletePredictionsRequest.build())
        try {
            Tasks.await(searchResult, 60, TimeUnit.SECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        locationSuggestionIds.clear()
        locationSuggestions.clear()
        return  if (searchResult.isSuccessful) {
            searchResult.result.autocompletePredictions.forEach {
                locationSuggestionIds.add(it.placeId)
                locationSuggestions.add(SearchResult(it.getPrimaryText(null).toString(), it.getSecondaryText(null).toString()))
            }
            locationSuggestions
        } else null
    }

    private var _isSearching: MutableLiveData<Boolean> = MutableLiveData()

    private var onItemClicked : ((searchResult: SearchResult)->Unit)? = null

    fun onSearchItemClicked(listener: (SearchResult)->Unit){
        onItemClicked =listener
    }

    val isSearching: LiveData<Boolean>
        get() = _isSearching

}