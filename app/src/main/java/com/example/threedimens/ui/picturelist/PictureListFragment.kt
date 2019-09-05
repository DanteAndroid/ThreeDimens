package com.example.threedimens.ui.picturelist

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.base.base.BaseFragment
import com.example.base.base.LoadStatus
import com.example.threedimens.R
import com.example.threedimens.ui.detail.PictureViewerActivity
import com.example.threedimens.ui.main.ApiType
import com.example.threedimens.utils.InjectorUtils
import com.example.threedimens.utils.Scrollable
import kotlinx.android.synthetic.main.fragment_picture_list.*
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
        PictureListAdapter(viewModel) { image, view, position ->
            PictureViewerActivity.startViewer(activity!!, view, image.url, image.type, position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_list, container, false)
    }

    override fun initView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val manager = recyclerView.layoutManager as StaggeredGridLayoutManager
        Handler().post { manager.invalidateSpanAssignments() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadMoreImages()
                    swipeRefresh.isRefreshing = true
                }
            }
        })
        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(context!!, R.color.colorPrimary),
            ContextCompat.getColor(context!!, R.color.colorAccent)
        )
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshImages()
        }
        viewModel.status.observe(this, Observer {
            when (it) {
                LoadStatus.LOADING -> {
//                    swipeRefresh.isRefreshing = true
                }
                LoadStatus.ERROR -> {
                    swipeRefresh.isRefreshing = false
                    recyclerView.snackbar(R.string.load_fail)
                }
                LoadStatus.DONE -> {
                    swipeRefresh.isRefreshing = false
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                }
            }
        })
        viewModel.pagedImages.observe(this, Observer {
            if (it.isEmpty()) return@Observer
            adapter.submitList(it)
        })
    }

    override fun scrollToTop() {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            if (manager.findLastVisibleItemPosition() > 20) {
                recyclerView.scrollToPosition(0)
            } else {
                recyclerView.smoothScrollToPosition(0)
            }
        } else {
            recyclerView.scrollToPosition(0)
        }
    }

    companion object {
        private const val ARG_API_TYPE = "api_type"

        fun newInstance(type: ApiType): PictureListFragment {
            return PictureListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_API_TYPE, type)
                }
            }
        }
    }
}