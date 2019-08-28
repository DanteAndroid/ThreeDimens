package com.example.threedimens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.base.BaseFragment
import com.example.threedimens.data.ApiType
import com.example.threedimens.ui.picturelist.PictureListFragment
import com.example.threedimens.ui.picturelist.PicturePagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_tabs.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class MainTabsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_tabs, container, false)
    }

    override fun initView() {
        val adapter = PicturePagerAdapter(
            resources.getStringArray(R.array.main_titles),
            ApiType.mainTypes,
            activity!!.supportFragmentManager
        )
        view_pager.adapter = adapter
        tabs.setupWithViewPager(view_pager)
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