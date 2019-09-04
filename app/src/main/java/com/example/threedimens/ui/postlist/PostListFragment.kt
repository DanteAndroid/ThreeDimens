package com.example.threedimens.ui.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.base.BaseFragment
import com.example.base.base.LoadStatus
import com.example.threedimens.MainDrawerActivity
import com.example.threedimens.R
import com.example.threedimens.net.API
import com.example.threedimens.ui.main.ApiType
import com.example.threedimens.ui.picturelist.PictureListFragment
import com.example.threedimens.utils.InjectorUtils
import com.example.threedimens.utils.setBack
import kotlinx.android.synthetic.main.fragment_picture_list.*
import org.jetbrains.anko.design.snackbar

/**
 * A placeholder fragment containing a simple view.
 */
class PostListFragment private constructor() : BaseFragment() {

    //    private val args: PicturePageFragmentArgs by navArgs()
    private val apiType: ApiType by lazy {
        arguments?.getParcelable<ApiType>(ARG_API_TYPE) as ApiType
    }

    private val viewModel: PostListViewModel by viewModels {
        InjectorUtils.providePostsViewModelFactory(apiType)
    }

    private val adapter = PostListAdapter { post, view, position ->
        apiType.path = post.postUrl.removePrefix(API.MZ_BASE)
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_open_enter, R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter, R.anim.fragment_close_exit
                )
                .replace(
                    R.id.navHostFragment,
                    PictureListFragment.newInstance(apiType), null
                )
                .addToBackStack(null).commit()

            it as MainDrawerActivity
            it.setBack(true)
        }
//        findNavController().navigate(
//            R.id.action_postsTabsFragment_to_pictureListFragment,
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
        println("${javaClass.simpleName} ${apiType.type}")

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                println("load onScrollStateChange ${recyclerView.canScrollVertically(1)}")
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
            viewModel.refreshPosts()
        }
        viewModel.refreshPosts()
        viewModel.status.observe(this, Observer {
            when (it) {
                LoadStatus.LOADING -> {
                    swipeRefresh.isRefreshing = true
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
            println("load type ${it?.first()?.type} ${it.size}")
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

        fun newInstance(type: ApiType): PostListFragment {
            return PostListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_API_TYPE, type)
                }
            }
        }
    }
}