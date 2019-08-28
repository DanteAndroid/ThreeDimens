package com.example.threedimens.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
@Entity
@JsonClass(generateAdapter = true)
data class Image(
    val id: Int = 0,
    var type: String,
    @PrimaryKey val url: String,
    val timestamp: Date = Date(),
    val title: String = ""
) {


}