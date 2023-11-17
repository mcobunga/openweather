package com.bonface.openweather.ui.favorites

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.databinding.ItemFavoritePlacesBinding
import com.bonface.openweather.databinding.ItemWeatherForecastBinding
import com.bonface.openweather.ui.home.ForecastAdapter
import com.bonface.openweather.utils.gone
import com.bonface.openweather.utils.roundOffDecimal
import com.bonface.openweather.utils.show
import timber.log.Timber
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
    var currentUserLocation: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoritePlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = differ.currentList[position]
        with(holder.binding) {
            location.text = holder.itemView.context.getString(R.string.fav_location, place.location, place.country)
            locationTemp.text = place.getTemperature()
            locationWeather.text = place.weatherMain
            lastUpdatedAt.text = place.lastUpdated()
            if (currentUserLocation != null && (roundOffDecimal(currentUserLocation!!.latitude) == place.latitude && roundOffDecimal(currentUserLocation!!.longitude) == place.longitude)) {
                currentLocation.show()
                lastUpdatedAt.gone()
            } else {
                currentLocation.gone()
                lastUpdatedAt.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}