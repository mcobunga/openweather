package com.bonface.openweather.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonface.openweather.R
import com.bonface.openweather.databinding.ActivityPlacesSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacesSearchBinding
    private var entryPoint: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }





    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

}