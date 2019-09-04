package com.example.threedimens.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.ui.picturelist.PictureListFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_tabs.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
abstract class PicturesTabsFragment : BaseFragment() {

    abstract fun isPost(): Boolean
    abstract fun getTitleArrayId(): Int
    abstract fun getApiTypeArray(): Array<ApiType>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_tabs, container, false)
    }

    override fun initView() {
        val adapter = TabsPagerAdapter(
            isPost(),
            resources.getStringArray(getTitleArrayId()),
            getApiTypeArray(),
            activity!!.supportFragmentManager
        )
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        tabs.tabMode = if (displayFixTabs()) TabLayout.MODE_FIXED else TabLayout.MODE_SCROLLABLE

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                val fragment = adapter.currentFragment as? PictureListFragment
                fragment?.scrollToTop()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    private fun displayFixTabs(): Boolean {
        if (ScreenUtils.isLandscape() || getApiTypeArray().size <= 4) {
            return true
        }
        return false
    }

}