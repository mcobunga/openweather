package com.bonface.openweather.ui.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonface.openweather.databinding.ActivityFavoritePlacesBinding
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
        setupAdapter()
    }

    private fun setupAdapter() {
        with(binding) {
            val adapter = PlacesFragmentAdapter(supportFragmentManager, lifecycle)
            viewpager.adapter = adapter
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

}