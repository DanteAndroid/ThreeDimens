package com.dante.threedimens.ui.main

import com.dante.threedimens.R

/**
 * @author Dante
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

class BooruTabsFragment : PicturesTabsFragment() {

    override fun isPost(): Boolean = false

    override fun getTitleArrayId(): Int = R.array.booru_titles

    override fun getApiTypeArray(): Array<ApiType> = ApiType.menuBooru

}

class ShtTabsFragment : PicturesTabsFragment() {

    override fun isPost(): Boolean = true

    override fun getTitleArrayId(): Int = R.array.sht_titles

    override fun getApiTypeArray(): Array<ApiType> = ApiType.menuSehuatang

}