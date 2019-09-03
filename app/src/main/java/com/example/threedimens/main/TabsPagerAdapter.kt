package com.example.threedimens.main

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.threedimens.ui.picturelist.PictureListFragment
import com.example.threedimens.ui.postlist.PostListFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabsPagerAdapter(
    private val isPost: Boolean,
    private val titles: Array<String>,
    private val apiTypes: Array<ApiType>,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var currentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment {
        if (isPost) return PostListFragment.newInstance(apiTypes[position])
        return PictureListFragment.newInstance(
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