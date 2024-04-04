package com.test.data.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Query("SELECT * FROM PhotoEntity")
    fun getAll(): List<PhotoEntity>


    @Insert
    fun insert(vararg image: PhotoEntity)

    @Delete
    fun delete(image: PhotoEntity)
}