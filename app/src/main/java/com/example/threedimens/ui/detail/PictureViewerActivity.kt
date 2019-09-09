package com.example.threedimens.ui.detail

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
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.base.base.BaseActivity
import com.example.base.base.BaseApplication
import com.example.threedimens.R
import com.example.threedimens.data.Image
import com.example.threedimens.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_viewer.*

class PictureViewerActivity(override val layoutResId: Int = R.layout.activity_viewer) :
    BaseActivity() {

    companion object {
        private const val ARG_TYPE = "type"
        private const val ARG_POSITION = "transPosition"
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
                    putExtra(ARG_POSITION, position)
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

    private val transPosition: Int by lazy { intent!!.getIntExtra(ARG_POSITION, 0) }
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

        viewModel.getImages().observe(this, Observer {
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
        super.onBackPressed()
    }


    override fun supportFinishAfterTransition() {
        if (transPosition == currentPosition) {
            super.supportFinishAfterTransition()
        } else {
            finish()
        }
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
