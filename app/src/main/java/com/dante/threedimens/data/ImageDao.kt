package com.dante.threedimens.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

/**
 * @author Dante
 * 2019-08-23
 */
@Dao
interface ImageDao {
    //    order by timestamp DESC
    @Query("select * from image where type = :type ")
    fun getPagedImages(type: String): DataSource.Factory<Int, Image>

    @Query("select * from image where type = :type ")
    fun getImages(type: String): LiveData<List<Image>>

    @Query("select * from image where id = :id")
    fun getImage(id: String): LiveData<Image>

    @Update
    fun update(image: Image)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(image: List<Image>): Array<Long>

    @Delete
    fun delete(image: Image)

    @Query("delete from image where type = :type")
    fun deleteAll(type: String)
}