package com.dante.threedimens.utils

import android.app.Activity
import com.dante.base.base.BaseApplication
import com.dante.threedimens.data.AppDatabase
import com.dante.threedimens.ui.detail.PictureViewerRepository
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.ui.picturelist.ImageRepository
import com.dante.threedimens.ui.postlist.PostRepository
import com.dante.threedimens.utils.viewmodel.AppViewModelFactory
import com.dante.threedimens.utils.viewmodel.PictureViewerViewModelFactory
import com.dante.threedimens.utils.viewmodel.PicturesViewModelFactory
import com.dante.threedimens.utils.viewmodel.PostsViewModelFactory

/**
 * @author Dante
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

    fun provideAppViewModelFactory(activity: Activity): AppViewModelFactory {
        return AppViewModelFactory(activity)
    }
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

