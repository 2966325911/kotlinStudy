package com.kangengine.kotlinstudydemo.ui.activity

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.glide.GlideApp
import com.kangengine.kotlinstudydemo.mvp.contract.CategoryDetailContract
import com.kangengine.kotlinstudydemo.mvp.presenter.CategoryDetailPresenter
import com.kangengine.kotlinstudydemo.ui.adapter.CategoryDetailAdapter
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_category_detail.*
import kotlinx.android.synthetic.main.fragment_hot.*

class CategoryDetailActivity : BaseActivity(),CategoryDetailContract.View{
    private val mPresenter by lazy { CategoryDetailPresenter() }

    private var categoryData : CategoryBean ?= null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mAdapter by lazy { CategoryDetailAdapter(this,itemList,
        R.layout.item_category_detail) }

    private var loadingMore = false
    init {
        mPresenter.attachView(this)
    }
    override fun layoutId(): Int {
        return R.layout.activity_category_detail
    }

    override fun initData() {
        categoryData = intent.getSerializableExtra(Constants.BUNDLE_CATEGORY_DATA) as
                CategoryBean?
    }

    override fun initView() {
        setSupportActionBar(category_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        category_toolbar.setNavigationOnClickListener{finish()}
        //  加载headerImage
        GlideApp.with(this)
            .load(categoryData?.headerImage)
            .placeholder(R.color.color_darker_gray)
            .into(imageView)

        tv_category_desc.text = "#${categoryData?.description}#"

        collapsing_toolbar_layout.title = categoryData?.name
        collapsing_toolbar_layout.setExpandedTitleColor(Color.WHITE)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.BLACK)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        //实现自动加载
        mRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(!loadingMore && lastVisibleItem == (itemCount -1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        //状态透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)
    }


    override fun start() {
        //获取当前分类列表
        categoryData?.id?.let { mPresenter.getCategoryDetailList(it) }
    }

    override fun setCateDetailList(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mAdapter.addData(itemList)
    }

    override fun showError(errorMsg: String) {
        category_multipleStatusView.showError()
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
