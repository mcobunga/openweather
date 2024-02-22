package com.bonface.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bonface.openweather.data.local.TABLE_FAVORITE_PLACES
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePlacesDao {
    @Query("SELECT * FROM $TABLE_FAVORITE_PLACES")
    fun getFavoritePlaces(): Flow<List<FavoritePlacesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity)

    @Delete
    suspend fun removeLocation(favoritePlacesEntity: FavoritePlacesEntity)

    @Query("DELETE FROM $TABLE_FAVORITE_PLACES")
    suspend fun deleteAllPlaces()

    @Query("SELECT EXISTS (SELECT 1 FROM $TABLE_FAVORITE_PLACES WHERE latitude = :latitude AND longitude = :longitude)")
    fun isLocationAlreadyExists(latitude: Double, longitude: Double): Boolean

}