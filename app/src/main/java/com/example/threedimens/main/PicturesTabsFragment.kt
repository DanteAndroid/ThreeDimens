package com.example.threedimens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.data.ApiType
import com.example.threedimens.ui.picturelist.PictureListFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_tabs.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PicturesTabsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_tabs, container, false)
    }

    override fun initView() {
        val adapter = PicturesPagerAdapter(
            resources.getStringArray(R.array.main_titles),
            ApiType.mainTypes,
            activity!!.supportFragmentManager
        )
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE

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

}