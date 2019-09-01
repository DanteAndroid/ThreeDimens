package com.example.threedimens.utils

import android.os.Handler
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.threedimens.R
import com.example.threedimens.main.PicturesTabsFragment
import com.example.threedimens.main.PostsTabsFragment

/**
 * @author Du Wenyu
 * 2019-09-01
 */
class FragmentHelper(private val manager: FragmentManager, private val containerId: Int) {
    private var currentFragment: Fragment? = null
    private val fragmentSparseArray: SparseArray<Fragment> = SparseArray(4)

    init {
        fragmentSparseArray.put(R.id.picturesTabsFragment, PicturesTabsFragment())
        fragmentSparseArray.put(R.id.postsTabsFragment, PostsTabsFragment())
    }

    fun setMainFragment(id: Int, isFirst: Boolean) {
        val fragment = fragmentSparseArray.get(id)
        val transaction = manager.beginTransaction()
        if (isFirst) {
            transaction.add(containerId, fragment, id.toString())
        } else {
            transaction.replace(containerId, fragment, id.toString())
        }
        transaction.commit()
        currentFragment = fragment
    }

    fun switchMenu(id: Int) {
        val fragment = fragmentSparseArray.get(id)
        if (fragment == null || currentFragment === fragment) {
            return
        }
        val old = currentFragment

        val transaction = manager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        if (old != null) {
            transaction.hide(old)
        }

        if (fragment.isAdded) {
            transaction.show(fragment)
            transaction.commit()
        } else {
            Handler().postDelayed({
                transaction.add(R.id.container, fragment, id.toString())
                transaction.commit()
            }, DRAWER_CLOSE_DELAY)
        }
        currentFragment = fragment
    }
}