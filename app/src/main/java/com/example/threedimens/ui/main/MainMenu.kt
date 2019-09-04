package com.example.threedimens.ui.main

import com.example.threedimens.R

/**
 * @author Du Wenyu
 * 2019-09-03
 */
class MainTabsFragment : PicturesTabsFragment() {

    override fun isPost(): Boolean = false

    override fun getTitleArrayId(): Int = R.array.main_titles

    override fun getApiTypeArray(): Array<ApiType> = ApiType.menuGank
}

class MeiziTabsFragment : PicturesTabsFragment() {

    override fun isPost(): Boolean = true

    override fun getTitleArrayId(): Int = R.array.meizi_titles

    override fun getApiTypeArray(): Array<ApiType> = ApiType.menuMeizitu

}

class WallTabsFragment : PicturesTabsFragment() {

    override fun isPost(): Boolean = false

    override fun getTitleArrayId(): Int = R.array.wall_titles

    override fun getApiTypeArray(): Array<ApiType> = ApiType.menuWallHaven

}