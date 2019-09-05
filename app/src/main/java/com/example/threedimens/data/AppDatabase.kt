package com.example.threedimens.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.threedimens.utils.DATABASE_NAME

/**
 * @author Dante
 * 2019-08-23
 */
@Database(entities = [Image::class, Post::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}