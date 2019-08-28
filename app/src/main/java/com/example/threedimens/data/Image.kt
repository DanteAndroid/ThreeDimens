package com.example.threedimens.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
@Keep
@Entity
data class Image(
    val id: Int = 0,
    var type: String,
    @PrimaryKey val url: String,
    @Json(name = "publishedAt")
    val timestamp: Date? = null,
    val title: String = ""
)