package com.example.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author Du Wenyu
 * 2019-08-19
 */
abstract class BaseFragment : Fragment() {

    private var isDataLoaded = false
    private var isViewCreated = false

    /**
     * 初始化view，在onViewCreated中调用
     */
    abstract fun initView()

    /**
     * 懒加载，fragment 可见后调用
     */
    open fun lazyLoad(){}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        initView()
        lazyLoadIfPrepared()
    }

    private fun lazyLoadIfPrepared() {
        if (userVisibleHint && isViewCreated && !isDataLoaded) {
            lazyLoad()
            isDataLoaded = true
        }
    }

}