package com.bonface.openweather.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.databinding.ItemFavoritePlacesBinding
import com.bonface.openweather.databinding.ItemWeatherForecastBinding
import com.bonface.openweather.ui.home.ForecastAdapter
import javax.inject.Inject

class PlacesListAdapter @Inject constructor() : RecyclerView.Adapter<PlacesListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFavoritePlacesBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ForecastEntity>() {
        override fun areItemsTheSame(oldList: ForecastEntity, newList: ForecastEntity): Boolean {
            return oldList.latitude == newList.latitude
        }
        override fun areContentsTheSame(oldList: ForecastEntity, newList: ForecastEntity): Boolean {
            return oldList == newList
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoritePlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = differ.currentList[position]
        with(holder.binding) {
            location.text = forecast.getDay()
            locationTemp.text = forecast.getTemperature()
            locationWeather.text = forecast.weatherMain
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}