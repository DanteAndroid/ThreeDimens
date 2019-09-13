package com.dante.threedimens.ui.postlist

import com.dante.base.base.BaseActivity
import com.dante.threedimens.R
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.ui.picturelist.PictureListFragment

class PostDetailActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_post_detail

    override fun enableBack(): Boolean = true

    override fun initView() {
        val apiType = intent.getParcelableExtra(PostListFragment.ARG_API_TYPE) as ApiType
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                PictureListFragment.newInstance(apiType)
            )
            .commitAllowingStateLoss()
    }

}
