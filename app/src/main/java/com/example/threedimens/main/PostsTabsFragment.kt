package com.example.threedimens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.data.ApiType
import com.example.threedimens.ui.postlist.PostListFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_tabs.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PostsTabsFragment : BaseFragment() {

    private lateinit var adapter: PostsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_tabs, container, false)
    }

    override fun initView() {
        adapter = PostsPagerAdapter(
            resources.getStringArray(R.array.mz_titles),
            ApiType.meizituTypes,
            activity!!.supportFragmentManager
        )
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        tabs.tabMode = TabLayout.MODE_FIXED

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                val fragment = adapter.currentFragment as? PostListFragment
                fragment?.scrollToTop()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
            }

        })
    }

}