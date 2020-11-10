package com.dante.threedimens.ui.detail

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dante.base.base.BaseActivity
import com.dante.base.base.BaseApplication
import com.dante.threedimens.R
import com.dante.threedimens.data.Image
import com.dante.threedimens.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_viewer.*

class PictureViewerActivity(override val layoutResId: Int = R.layout.activity_viewer) :
    BaseActivity() {

    companion object {
        private const val ARG_TYPE = "type"
        private const val ARG_TRANS_POSITION = "transPosition"
        private const val REQUEST_VIEW = 1

        fun startViewer(
            activity: Activity,
            sharedView: View,
            url: String,
            type: String,
            position: Int
        ) {
            val intent =
                Intent(BaseApplication.instance(), PictureViewerActivity::class.java).apply {
                    putExtra(ARG_TYPE, type)
                    putExtra(ARG_TRANS_POSITION, position)
                }
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, sharedView, url
            )
            ActivityCompat.startActivityForResult(
                activity,
                intent,
                REQUEST_VIEW,
                options.toBundle()
            )
//            activity.startActivity(intent)
        }
    }

    private lateinit var adapter: DetailPagerAdapter
    private var hasInit: Boolean = false
    private lateinit var type: String

    private val transPosition: Int by lazy { intent!!.getIntExtra(ARG_TRANS_POSITION, 0) }
    private var currentPosition: Int = 0

    val viewModel: PictureViwerViewModel by viewModels {
        InjectorUtils.providePictureViewerViewModelFactory(type)
    }

    override fun enableBack(): Boolean = true

    override fun initView() {
        supportPostponeEnterTransition()

        type = intent!!.getStringExtra(ARG_TYPE)!!
        currentPosition = transPosition

        adapter = DetailPagerAdapter(fragmentManager = supportFragmentManager)

        viewModel.getImages().observe(this, {
            adapter.setNewData(it)
            initViewPager(adapter)
        })
    }


    private fun initViewPager(adapter: DetailPagerAdapter) {
        if (hasInit) return
        pager.adapter = adapter
        pager.currentItem = transPosition
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }
        })
        hasInit = true
    }

    override fun onBackPressed() {
        adapter.currentFragment?.restoreImage()
        if (shouldShowTransition()) {
            super.supportFinishAfterTransition()
        } else {
            finish()
        }
    }


    private fun shouldShowTransition(): Boolean {
        adapter.currentFragment?.let {
            if (transPosition == currentPosition && !it.dontShowTransition) {
                return true
            }
        }
        return false
    }

    inner class DetailPagerAdapter(
        private val images: MutableList<Image> = mutableListOf(),
        fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        var currentFragment: PictureDetailFragment? = null

        override fun getItem(position: Int): Fragment {
            return PictureDetailFragment.newInstance(
                images[position].id, transPosition == position
            )
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            super.setPrimaryItem(container, position, `object`)
            if (currentFragment !== `object`) {
                currentFragment = `object` as PictureDetailFragment
            }
        }

        override fun getCount(): Int = images.size

        fun setNewData(list: List<Image>) {
            images.clear()
            images.addAll(list)
            notifyDataSetChanged()
        }
    }

}
