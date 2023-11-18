package com.bonface.openweather.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.databinding.ItemFavoritePlacesBinding
import com.bonface.openweather.utils.getTemperature
import com.bonface.openweather.utils.hide
import com.bonface.openweather.utils.lastUpdated
import com.bonface.openweather.utils.show
import javax.inject.Inject

class PlacesListAdapter @Inject constructor() : RecyclerView.Adapter<PlacesListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFavoritePlacesBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<FavoritePlacesEntity>() {
        override fun areItemsTheSame(oldList: FavoritePlacesEntity, newList: FavoritePlacesEntity): Boolean {
            return oldList.latitude == newList.latitude
        }
        override fun areContentsTheSame(oldList: FavoritePlacesEntity, newList: FavoritePlacesEntity): Boolean {
            return oldList == newList
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoritePlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = differ.currentList[position]
        with(holder.binding) {
            location.text = holder.itemView.context.getString(R.string.fav_location, place.location, place.country)
            locationTemp.text = getTemperature(place.temp)
            locationWeather.text = place.weatherMain
            lastUpdatedAt.text = lastUpdated(place.lastUpdatedAt)
            if (place.isCurrentLocation == true ) {
                currentLocation.show()
                lastUpdatedAt.hide()
            } else {
                currentLocation.hide()
                lastUpdatedAt.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}