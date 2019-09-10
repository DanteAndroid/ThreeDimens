package com.dante.threedimens.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

/**
 * @author Dante
 * 2019-08-23
 */
@Keep
@Entity(
//    foreignKeys = [ForeignKey(
//        entity = Post::class,
//        parentColumns = ["postUrl"],
//        childColumns = ["post"]
//    )]
)
data class Image(
    @Json(name = "_id")
    @PrimaryKey
    val id: String,
    var type: String,
    val url: String,
    val originalUrl: String = "",
    @Json(name = "publishedAt")
    val timestamp: Date? = null,
    val post: String = ""
)