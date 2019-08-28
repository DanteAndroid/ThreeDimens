package com.example.threedimens.ui.picturelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.base.BaseFragment
import com.example.base.base.LoadStatus
import com.example.threedimens.R
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.Image
import com.example.threedimens.ui.detail.PictureViewerActivity
import com.example.threedimens.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.design.snackbar

/**
 * A placeholder fragment containing a simple view.
 */
class PictureListFragment private constructor() : BaseFragment() {

    //    private val args: PicturePageFragmentArgs by navArgs()
    private val apiType: ApiType by lazy {
        arguments?.getSerializable(ARG_API_TYPE) as ApiType
    }

    private val viewModel: PictureListViewModel by viewModels {
        InjectorUtils.providePageViewModelFactory(apiType)
    }

    private val adapter = PictureListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun initView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(context!!, R.color.colorPrimary),
            ContextCompat.getColor(context!!, R.color.colorAccent)
        )
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshImages()
        }
        adapter.setOnLoadMoreListener({ viewModel.loadMoreImages() }, recyclerView)
        adapter.setOnItemClickListener { adapter, view, position ->
            val image = adapter.data[position] as? Image
            image?.let {
                PictureViewerActivity.startViewer(activity!!, view, image.url, it.type, position)
            }
        }
        viewModel.status.observe(this, Observer {
            when (it) {
                LoadStatus.LOADING -> {
                    // BaseAdapter 有loading，这里就不加了
                    // swipeRefresh.isRefreshing = true
                }
                LoadStatus.ERROR -> {
                    swipeRefresh.isRefreshing = false
                    recyclerView.snackbar(R.string.load_fail)
                    if (viewModel.isLoadMore()) {
                        adapter.loadMoreFail()
                    }
                }
                LoadStatus.DONE -> {
                    swipeRefresh.isRefreshing = false
                    adapter.loadMoreComplete()
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                    adapter.loadMoreComplete()
                }
            }
        })
        viewModel.images.observe(this, Observer<List<Image>> {
            if (it.isEmpty()) return@Observer
            println("apiType ${it?.first()?.type} ${it.size}")
            adapter.submitList(it)
        })
    }

    fun scrollToTop() {
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
                    putSerializable(ARG_API_TYPE, type)
                }
            }
        }
    }
}