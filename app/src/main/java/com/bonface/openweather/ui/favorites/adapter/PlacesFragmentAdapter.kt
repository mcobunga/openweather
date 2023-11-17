package com.bonface.openweather.ui.favorites.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bonface.openweather.ui.favorites.FavoritePlacesFragment
import com.bonface.openweather.ui.favorites.FavoritesMapsFragment

class PlacesFragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    private val fragments = listOf(
        FavoritePlacesFragment(),
        FavoritesMapsFragment()
    )

}