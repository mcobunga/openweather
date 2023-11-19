package com.bonface.openweather.ui.searchResult

import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.databinding.FragmentSearchedLocationWeatherBottomSheetBinding
import com.bonface.openweather.mappers.toWeatherEntity
import com.bonface.openweather.ui.home.MainActivity
import com.bonface.openweather.ui.viewmodel.WeatherViewModel
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.getCurrentWeatherBackgroundColor
import com.bonface.openweather.utils.getCurrentWeatherImage
import com.bonface.openweather.utils.getTemperature
import com.bonface.openweather.utils.lastUpdated
import com.bonface.openweather.utils.toast
import com.bonface.openweather.utils.updateStatusBarColor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

const val SEARCHED_LOCATION_WEATHER_BOTTOM_SHEET = "SearchedLocationWeatherBottomSheet"
const val LOCATION_WEATHER_INFO = "location_weather"

@AndroidEntryPoint
class SearchedLocationWeatherBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentSearchedLocationWeatherBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by viewModels()

    companion object {
        private var currentWeather: CurrentWeather? = null
        operator fun invoke(
            location: CurrentWeather? = null
        ) = SearchedLocationWeatherBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(LOCATION_WEATHER_INFO, location)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val bottomSheet: FrameLayout = (it as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

                BottomSheetBehavior
                    .from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED)
            }
            setCanceledOnTouchOutside(false)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchedLocationWeatherBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            currentWeather = getParcelable(LOCATION_WEATHER_INFO)
        }
        setButtonListener()
        setViews()
    }

    private fun setViews() {
        val weather = currentWeather?.toWeatherEntity()
        if (weather != null) {
            updateWeatherViews(weather)
        } else {
            dismiss()
        }
    }

    private fun updateWeatherViews(weather: CurrentWeatherEntity) {
        with(binding) {
            weather.apply {
                bottomSheetBg.setBackgroundColor(ContextCompat.getColor(requireContext(), getCurrentWeatherBackgroundColor(this.weatherId)))
                currentWeatherLayout.background = ContextCompat.getDrawable(requireContext(), getCurrentWeatherImage(this.weatherId))
                currentLocation.text = getString(R.string.user_location, this.name.toString(), this.country.toString())
                currentTemp.text = getTemperature(this.temp)
                currentWeather.text = this.weatherMain
                lastUpdated.text = getString(R.string.last_updated, lastUpdated(this.lastUpdatedAt))
                minTemperatureValue.text = getTemperature(this.minTemp)
                currentTemperatureValue.text = getTemperature(this.temp)
                maxTemperatureValue.text = getTemperature(this.maxTemp)
                activity?.updateStatusBarColor(getCurrentWeatherImage(this.weatherId))
            }
        }
    }

    private fun setButtonListener() {
        binding.saveLocation.setOnClickListener {
            currentWeather?.let { location -> weatherViewModel.saveLocationToFavorites(location) }
            activity?.toast(getString(R.string.add_to_favorites_success_message))
            dismiss()
        }
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}