package com.example.threedimens.ui.picturelist

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.threedimens.data.ApiType


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class PicturePagerAdapter(
    private val titles: Array<String>,
    private val apiTypes: List<ApiType>,
    fm: FragmentManager
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var currentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment {
        return PictureListFragment.newInstance(apiTypes[position])
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