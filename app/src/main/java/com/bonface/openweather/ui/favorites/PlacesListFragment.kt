package com.bonface.openweather.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.databinding.FragmentPlacesListBinding
import com.bonface.openweather.ui.home.ForecastAdapter
import com.bonface.openweather.ui.home.MainViewModel
import com.bonface.openweather.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlacesListFragment : Fragment() {
    private var _binding: FragmentPlacesListBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var placesListAdapter: PlacesListAdapter

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getFavoritePlaces()
    }

    private fun getFavoritePlaces() {
        mainViewModel.favoritePlaces.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()
                    updateFavoritePlacesUi(resource.data)
                }
                is Resource.Error -> {
                    hideLoading()
                    showSnackbarErrorMessage(resource.message.toString())
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun updateFavoritePlacesUi(data: WeatherForecast?) {
        placesListAdapter.differ.submitList(data?.daily)
    }

    private fun setupAdapter() {
        with(binding) {
            favoritePlacesRv.apply {
                adapter = placesListAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun showLoading() {
        binding.loadingLayout.apply {
            visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.loadingLayout.apply {
            visibility = View.GONE
        }
    }

    private fun showSnackbarErrorMessage(message: String) {
        binding.favoritesLayout.apply {
            Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}