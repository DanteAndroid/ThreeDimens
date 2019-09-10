package com.dante.threedimens.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dante.threedimens.ui.postlist.PostListViewModel
import com.dante.threedimens.ui.postlist.PostRepository

/**
 * @author Dante
 * 2019-08-23
 */
class PostsViewModelFactory(private val repository: PostRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(repository) as T
    }
}