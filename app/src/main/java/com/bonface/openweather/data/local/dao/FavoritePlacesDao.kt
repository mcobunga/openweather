package com.bonface.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bonface.openweather.data.local.TABLE_FAVORITE_PLACES
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePlacesDao {
    @Query("SELECT * FROM $TABLE_FAVORITE_PLACES ORDER BY is_current DESC")
    fun getFavoritePlaces(): Flow<List<FavoritePlacesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoritePlace(locationEntity: FavoritePlacesEntity)

    @Delete
    suspend fun removeFavoritePlace(locationEntity: FavoritePlacesEntity)

    @Query("DELETE FROM $TABLE_FAVORITE_PLACES")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_FAVORITE_PLACES WHERE is_current =:current")
    fun getCurrentUserLocation(current: Int = 1): Flow<List<FavoritePlacesEntity>>

}