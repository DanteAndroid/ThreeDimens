package com.example.threedimens.utils

import com.example.base.base.BaseApplication
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.AppDatabase
import com.example.threedimens.ui.detail.PictureViwerRepository
import com.example.threedimens.ui.picturelist.ImageRepository
import com.example.threedimens.viewmodel.PageViewModelFactory
import com.example.threedimens.viewmodel.PictureViwerViewModelFactory

/**
 * @author Du Wenyu
 * 2019-08-23
 */
object InjectorUtils {

    fun providePageViewModelFactory(apiType: ApiType): PageViewModelFactory {
        return PageViewModelFactory(provideImageRepository(apiType))
    }

    fun providePictureViwerViewModelFactory(type: String): PictureViwerViewModelFactory {
        return PictureViwerViewModelFactory(PictureViwerRepository(type, provideAppDatabase().imageDao()))
    }

    fun provideImageRepository(apiType: ApiType): ImageRepository {
        return ImageRepository(apiType, provideAppDatabase().imageDao())
    }

    fun providePictureViwerRepository(type: String): PictureViwerRepository {
        return PictureViwerRepository(type, provideAppDatabase().imageDao())
    }

    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getInstance(BaseApplication.instance())
    }
}