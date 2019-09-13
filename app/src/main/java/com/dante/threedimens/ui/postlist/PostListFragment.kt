package com.dante.threedimens.ui.postlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dante.base.base.BaseFragment
import com.dante.base.base.LoadStatus
import com.dante.threedimens.R
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.InjectorUtils
import com.dante.threedimens.utils.Scrollable
import com.dante.threedimens.utils.getLastPosition
import kotlinx.android.synthetic.main.fragment_picture_list.*
import org.jetbrains.anko.design.snackbar

/**
 * A placeholder fragment containing a simple view.
 */
class PostListFragment private constructor() : BaseFragment(), Scrollable {

    //    private val args: PicturePageFragmentArgs by navArgs()
    private val apiType: ApiType by lazy {
        arguments?.getParcelable<ApiType>(ARG_API_TYPE) as ApiType
    }

    private val viewModel: PostListViewModel by viewModels {
        InjectorUtils.providePostsViewModelFactory(apiType)
    }
    private lateinit var manager: GridLayoutManager

    private val adapter = PostListAdapter { post, view, position ->
        apiType.path = post.postUrl.removePrefix(apiType.site.baseUrl)
        activity?.startActivity(Intent(context, PostDetailActivity::class.java).apply {
            putExtra(ARG_API_TYPE, apiType)
        })
//        findNavController().navigate(
//            R.id.action_postListFragment_to_pictureListFragment,
//            bundleOf(
//                ARG_API_TYPE to apiType
//            )
//        )
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
        manager = GridLayoutManager(context, apiType.site.spanCount)
        recyclerView.layoutManager = manager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadMorePosts()
                    swipeRefresh.isRefreshing = true
                }
            }
        })
        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(context!!, R.color.colorPrimary),
            ContextCompat.getColor(context!!, R.color.colorAccent)
        )
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }
        viewModel.refreshPosts()
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
        viewModel.posts.observe(this, Observer {
            if (it.isEmpty()) return@Observer
            adapter.submitList(it)
        })
    }

    override fun scrollToTop() {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            if (manager.findLastVisibleItemPosition() > 60) {
                recyclerView.scrollToPosition(0)
            } else {
                recyclerView.smoothScrollToPosition(0)
            }
        } else {
            recyclerView.scrollToPosition(0)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveLastPosition(manager.getLastPosition())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            restoreLastPosition()
        }
    }

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

    companion object {
        const val ARG_API_TYPE = "api_type"

        fun newInstance(type: ApiType): PostListFragment {
            return PostListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_API_TYPE, type)
                }
            }
        }
    }
}