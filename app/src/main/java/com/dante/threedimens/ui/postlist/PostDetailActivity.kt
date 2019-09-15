package com.dante.threedimens.ui.postlist

import com.dante.base.base.BaseActivity
import com.dante.threedimens.R
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.ui.picturelist.PictureListFragment
import com.dante.threedimens.utils.ARG_API_TYPE
import com.dante.threedimens.utils.ARG_TITLE
import kotlinx.android.synthetic.main.activity_post_detail.*


class PostDetailActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_post_detail

    override fun enableBack(): Boolean = true

    override fun initView() {
        val apiType = intent.getParcelableExtra(ARG_API_TYPE) as ApiType
        val title = intent.getStringExtra(ARG_TITLE)
        val fragment = PictureListFragment.newInstance(apiType)
        setSupportActionBar(toolbar)
        toolbar.setOnClickListener { fragment.scrollToTop() }
        titleTV.text = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }


}
