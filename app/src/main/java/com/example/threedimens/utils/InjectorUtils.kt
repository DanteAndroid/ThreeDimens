package com.example.threedimens.utils

import com.example.base.base.BaseApplication
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.AppDatabase
import com.example.threedimens.ui.detail.PictureViewerRepository
import com.example.threedimens.ui.picturelist.ImageRepository
import com.example.threedimens.ui.postlist.PostRepository
import com.example.threedimens.viewmodel.PictureViewerViewModelFactory
import com.example.threedimens.viewmodel.PicturesViewModelFactory
import com.example.threedimens.viewmodel.PostsViewModelFactory

/**
 * @author Du Wenyu
 * 2019-08-23
 */
object InjectorUtils {

    fun providePageViewModelFactory(apiType: ApiType): PicturesViewModelFactory {
        return PicturesViewModelFactory(provideImageRepository(apiType))
    }

    fun providePostsViewModelFactory(apiType: ApiType): PostsViewModelFactory {
        return PostsViewModelFactory(providePostRepository(apiType))
    }

    fun providePictureViewerViewModelFactory(type: String): PictureViewerViewModelFactory {
        return PictureViewerViewModelFactory(providePictureViwerRepository(type))
    }

    private fun provideImageRepository(apiType: ApiType): ImageRepository {
        return ImageRepository(apiType, provideAppDatabase().imageDao())
    }

    private fun providePostRepository(apiType: ApiType): PostRepository {
        return PostRepository(apiType, provideAppDatabase().postDao())
    }

    private fun providePictureViwerRepository(type: String): PictureViewerRepository {
        return PictureViewerRepository(type, provideAppDatabase().imageDao())
    }

    private fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getInstance(BaseApplication.instance())
    }
}