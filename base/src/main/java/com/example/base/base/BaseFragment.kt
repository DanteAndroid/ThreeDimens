package com.example.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author Du Wenyu
 * 2019-08-19
 */
abstract class BaseFragment : Fragment() {

    /**
     * 初始化view，在onViewCreated中调用
     */
    abstract fun initView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

}