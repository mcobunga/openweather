package com.bonface.openweather.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bonface.openweather.databinding.ItemWeatherForecastBinding
import com.bonface.openweather.domain.model.DailyWeather
import com.bonface.openweather.utils.getDay
import com.bonface.openweather.utils.getForecastWeatherIcon
import com.bonface.openweather.utils.getTemperature
import javax.inject.Inject

class ForecastAdapter @Inject constructor() : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<DailyWeather>() {
        override fun areItemsTheSame(oldList: DailyWeather, newList: DailyWeather): Boolean {
            return oldList.latitude == newList.latitude
        }
        override fun areContentsTheSame(oldList: DailyWeather, newList: DailyWeather): Boolean {
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
            day.text = getDay(forecast.dayOfWeek!!)
            weather.setImageResource(getForecastWeatherIcon(forecast.weatherId))
            temperature.text = getTemperature(forecast.dayTemp)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}