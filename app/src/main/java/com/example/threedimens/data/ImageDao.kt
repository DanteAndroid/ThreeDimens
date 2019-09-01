package com.example.threedimens.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
@Dao
interface ImageDao {
    @Query("select * from image where type = :type order by timestamp DESC")
    fun getPagedImages(type: String): DataSource.Factory<Int, Image>

    @Query("select * from image where type = :type order by timestamp DESC")
    fun getImages(type: String): LiveData<List<Image>>

    @Query("select * from image where id = :id")
    fun getImage(id: Int): LiveData<Image>

    @Insert
    fun insert(image: Image)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(image: List<Image>)

    @Delete
    fun delete(image: Image)

    @Query("delete from image")
    fun deleteAll()
}