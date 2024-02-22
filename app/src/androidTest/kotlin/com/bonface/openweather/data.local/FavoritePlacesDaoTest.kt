package com.bonface.openweather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.utils.TestCreationUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class FavoritePlacesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_database")
    lateinit var database: OpenWeatherDatabase
    private lateinit var favoritePlacesDao: FavoritePlacesDao

    @Before
    fun setup() {
        hiltRule.inject()
        favoritePlacesDao = database.favoritePlacesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun saveFavoritePlaceToFavorites() = runBlocking {
        //given
        val favoritePlace = TestCreationUtils.getFavoritePlaces()

        //when
        favoritePlacesDao.saveFavoritePlace(favoritePlace.first())
        val favorites = favoritePlacesDao.getFavoritePlaces().first().toList()

        //verify
        MatcherAssert.assertThat(favorites.first().latitude, `is` (favoritePlace.first().latitude))

    }

}