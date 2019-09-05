package com.example.threedimens.data

import androidx.room.TypeConverter
import java.util.*

/**
 * @author Dante
 * 2019-08-23
 */
class RoomConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}