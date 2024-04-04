package com.test.data.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "alt") val alt: String,
    @ColumnInfo(name = "avg_color") val avgColor: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "liked") val liked: Boolean,
    @ColumnInfo(name = "photographer") val photographer: String,
    @ColumnInfo(name = "photographer_id") val photographerId: Int,
    @ColumnInfo(name = "photographer_url") val photographerUrl: String,
    @ColumnInfo(name = "src") val src: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "width") val width: Int
)