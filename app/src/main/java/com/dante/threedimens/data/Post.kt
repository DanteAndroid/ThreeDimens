package com.dante.threedimens.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Dante
 * 2019-08-31
 */
@Keep
@Entity
data class Post(
    @PrimaryKey val postUrl: String,
    val coverUrl: String = "",
    var type: String,
    val title: String,
    val content: String = ""
)