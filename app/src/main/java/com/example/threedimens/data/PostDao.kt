package com.example.threedimens.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author Du Wenyu
 * 2019-08-23
 */
@Dao
interface PostDao {

    @Query("select * from post where type = :type")
    fun getPosts(type: String): LiveData<List<Post>>

    @Query("select * from post where postUrl = :posUrl")
    fun getPost(posUrl: String): LiveData<Post>

    @Query("select * from image where post = :postUrl")
    fun getImagesOfPost(postUrl: String): LiveData<List<Image>>

    @Insert
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<Post>)

    @Query("delete from post")
    fun deleteAll()
}