package com.bonface.openweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.databinding.ItemWeatherForecastBinding
import timber.log.Timber
import javax.inject.Inject

class ForecastAdapter @Inject constructor() : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<DailyForecast>() {
        override fun areItemsTheSame(oldList: DailyForecast, newList: DailyForecast): Boolean {
            return oldList.dt == newList.dt
        }
        override fun areContentsTheSame(oldList: DailyForecast, newList: DailyForecast): Boolean {
            return oldList == newList
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = differ.currentList[position]
        holder.binding.apply {
            day.text = forecast.getDay()
            forecast.weather?.firstOrNull()?.getForecastWeatherIcon()
                ?.let { weather.setImageResource(it) }
            temperature.text = forecast.temp?.getTemperature()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}