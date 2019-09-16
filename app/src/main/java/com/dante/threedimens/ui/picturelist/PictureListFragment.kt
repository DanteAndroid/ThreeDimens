package com.dante.threedimens.ui.picturelist

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dante.base.base.BaseFragment
import com.dante.base.base.LoadStatus
import com.dante.threedimens.R
import com.dante.threedimens.ui.detail.PictureViewerActivity
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.ARG_API_TYPE
import com.dante.threedimens.utils.HIDE_LOAD_HINT_DELAY
import com.dante.threedimens.utils.InjectorUtils
import com.dante.threedimens.utils.Scrollable
import kotlinx.android.synthetic.main.fragment_picture_list.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar

/**
 * A placeholder fragment containing a simple view.
 */
class PictureListFragment : BaseFragment(), Scrollable {

    private val apiType: ApiType by lazy {
        arguments!!.getParcelable<ApiType>(ARG_API_TYPE) as ApiType
    }

    private val viewModel: PictureListViewModel by viewModels {
        InjectorUtils.providePageViewModelFactory(apiType)
    }

    private val adapter by lazy {
        PictureListAdapter(apiType) { image, view, position ->
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.snackbar(R.string.is_loading)
                return@PictureListAdapter
            }
            PictureViewerActivity.startViewer(activity!!, view, image.url, image.type, position)
        }
    }

    private lateinit var manager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_list, container, false)
    }

    override fun initView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        manager =
            StaggeredGridLayoutManager(apiType.site.spanCount, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = manager
        Handler().post { manager.invalidateSpanAssignments() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (viewModel.loadMoreImages()) {
                        swipeRefresh.isRefreshing = true
                    }
                }
            }
        })
        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(context!!, R.color.colorPrimary),
            ContextCompat.getColor(context!!, R.color.colorAccent)
        )
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshImages(true)
        }
        viewModel.status.observe(this, Observer {
            when (it) {
                LoadStatus.LOADING -> {
//                    swipeRefresh.isRefreshing = true
                }
                LoadStatus.FAIL -> {
                    setLoadingIndicator(false)
                    recyclerView.longSnackbar(R.string.load_fail)
                }
                LoadStatus.NET_ERROR -> {
                    setLoadingIndicator(true, R.drawable.ic_connection_error)
                }
                LoadStatus.SUCCESS -> {
                    setLoadingIndicator(false)
                }
                else -> {
                    setLoadingIndicator(false)
                }
            }
        })
        viewModel.pagedImages.observe(this, Observer {
            if (it.isEmpty()) {
                setLoadingIndicator(true, R.drawable.loading_animation)
            } else {
                adapter.submitList(it)
            }
        })
//        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
//            viewModel.saveLastPosition(manager.getLastPosition())
//        }
    }

    private fun setLoadingIndicator(show: Boolean, @DrawableRes drawable: Int = 0) {
        swipeRefresh.isRefreshing = false
        if (show) {
            val position =
                manager.findFirstCompletelyVisibleItemPositions(IntArray(manager.spanCount))[0]
            if (position < 0) {
                loadHint.isVisible = true
                loadHint.setImageResource(drawable)
            }
        } else {
            if (loadHint.isVisible) {
                loadHint.postDelayed({
                    loadHint.isVisible = false
                }, HIDE_LOAD_HINT_DELAY)
            }
        }
    }


//    override fun onPause() {
//        super.onPause()
//        viewModel.saveLastPosition(manager.getLastPosition())
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        if (savedInstanceState == null) {
//            restoreLastPosition()
//        }
//    }

    private fun restoreLastPosition() {
        val lastPosition = viewModel.getLastPosition()
        if (lastPosition > 0) {
            recyclerView.postDelayed({
                if (lastPosition < manager.spanCount * 20) {
                    manager.smoothScrollToPosition(
                        recyclerView,
                        RecyclerView.State(),
                        lastPosition
                    )
                } else {
                    manager.scrollToPosition(lastPosition)
                }
            }, 200)
        }
    }

    override fun scrollToTop() {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            if (manager.findLastVisibleItemPosition() > 30) {
                recyclerView.scrollToPosition(0)
            } else {
                recyclerView.smoothScrollToPosition(0)
            }
        } else {
            recyclerView.scrollToPosition(0)
        }
    }

    companion object {

        fun newInstance(type: ApiType): PictureListFragment {
            return PictureListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_API_TYPE, type)
                }
            }
        }
    }
}