package com.kangengine.kotlinstudydemo.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.mvp.contract.FollowContract
import com.kangengine.kotlinstudydemo.mvp.presenter.FollowPresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.FollowAdapter
import kotlinx.android.synthetic.main.fragment_follow.*


class FollowFragment : BaseFragment(), FollowContract.View {

    private var mTitle: String? = null

    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { FollowPresenter() }

    private val mFollowAdapter by lazy {
        activity?.let {
            FollowAdapter(it, itemList)
        }
    }

    companion object {
        fun getInstance(title: String): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    /**
     * 是否加载更多
     */
    private var loadingMore = false

    init {
        mPresenter.attachView(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_follow
    }

    override fun initView() {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mFollowAdapter
        //实现自动加载
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        mLayoutStatusView = multipleStatusView
    }

    override fun lazyLoad() {
        mPresenter.requestFollowList()
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun setFollowInfo(issue: HomeBean.Issue) {
        loadingMore = false
        itemList = issue.itemList
        mFollowAdapter?.addData(itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView.showNoNetwork()
        } else {
            multipleStatusView.showError()

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
