package com.kangengine.kotlinstudydemo.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.mvp.contract.RankContract
import com.kangengine.kotlinstudydemo.mvp.presenter.RankPresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.CategoryDetailAdapter
import kotlinx.android.synthetic.main.fragment_rank.*

class RankFragment : BaseFragment(),RankContract.View {

    private val mPresenter by lazy { RankPresenter() }

    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mAdapter by lazy { activity?.let { CategoryDetailAdapter(it,itemList,R.layout.item_category_detail) }}
    private var apiUrl: String?= null
    companion object {
        fun getInstance(apiUrl:String) : RankFragment {
            val fragment = RankFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.apiUrl = apiUrl

            return fragment
        }
    }

    init {
        mPresenter.attachView(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_rank
    }

    override fun initView() {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mAdapter
    }

    override fun lazyLoad() {
        if(!apiUrl.isNullOrEmpty()) {
            mPresenter.requestRankList(apiUrl!!)
        }
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
    }

    override fun setRanKList(itemList: ArrayList<HomeBean.Issue.Item>) {
        multipleStatusView.showContent()
        mAdapter?.addData(itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if(errorCode == ErrorStatus.NETWORK_ERROR) {
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
