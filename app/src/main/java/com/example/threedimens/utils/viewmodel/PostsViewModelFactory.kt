package com.example.threedimens.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.threedimens.ui.postlist.PostListViewModel
import com.example.threedimens.ui.postlist.PostRepository

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