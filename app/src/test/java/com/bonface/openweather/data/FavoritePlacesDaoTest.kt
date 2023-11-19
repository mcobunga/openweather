package com.bonface.openweather.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonface.openweather.base.OpenWeatherBaseTest
import com.bonface.openweather.utils.TestCreationUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritePlacesDaoTest: OpenWeatherBaseTest() {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `save favorite place to favorites table and verify`() = runBlocking {
        //given
        val favoritePlace = TestCreationUtils.getFavoritePlaces()

        //when
        favoritePlacesDao.saveFavoritePlace(favoritePlace.first())
        val favorites = favoritePlacesDao.getFavoritePlaces().first().toList()

        //verify
        MatcherAssert.assertThat(favorites.first().latitude, `is` (favoritePlace.first().latitude))

    }

}