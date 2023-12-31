package com.bonface.openweather.ui.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonface.openweather.R
import com.bonface.openweather.databinding.ActivityFavoritePlacesBinding
import com.bonface.openweather.ui.favorites.adapter.PlacesFragmentAdapter
import com.bonface.openweather.utils.onBackPressedCallback
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritePlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritePlacesBinding
    private val tabTitles = arrayOf("Favorites List", "Favorites Map")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritePlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setupAdapter()
        handleBackPress()
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback())
    }

    private fun setToolbar() {
        with(binding.toolbar) {
            setNavigationIcon(R.drawable.button_arrow_back)
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupAdapter() {
        with(binding) {
            val adapter = PlacesFragmentAdapter(supportFragmentManager, lifecycle)
            placesViewpager.adapter = adapter
            TabLayoutMediator(placesTabLayout, placesViewpager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

}