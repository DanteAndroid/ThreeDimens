package com.example.threedimens.main

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.threedimens.data.ApiType
import com.example.threedimens.ui.postlist.PostListFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class PostsPagerAdapter(
    private val titles: Array<String>,
    private val apiTypes: List<ApiType>,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var currentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment {
        return PostListFragment.newInstance(
            apiTypes[position]
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return apiTypes.size
    }
}