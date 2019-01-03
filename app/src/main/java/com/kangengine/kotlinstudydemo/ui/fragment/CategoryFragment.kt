package com.kangengine.kotlinstudydemo.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.mvp.contract.CategoryContract
import com.kangengine.kotlinstudydemo.mvp.presenter.CategoryPresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.CategoryAdapter
import com.kangengine.kotlinstudydemo.utils.DisplayManager
import kotlinx.android.synthetic.main.fragment_catefory.*

class CategoryFragment : BaseFragment(),CategoryContract.View {


    private val mPresenter by lazy { CategoryPresenter() }

    private var mCategoryList = ArrayList<CategoryBean>()

    private val mAdapter by lazy {
        activity?.let { activity?.let { CategoryAdapter(it,mCategoryList,R.layout.item_catergory) } }
    }

    private var mTitle : String ?= null

    companion object {
        fun getInstance(title : String) : CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            fragment.arguments  = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_catefory
    }

    @Suppress("DEPRECATION")
    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = GridLayoutManager(activity,2)
        mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildPosition(view)
                val offset = DisplayManager.dip2px(2f)!!
                outRect.set(if(position % 2 == 0) 0 else offset,offset,
                    if(position % 2 ==0) offset else 0,offset)
            }
        })
    }

    override fun lazyLoad() {
        //获取分类信息
        mPresenter.getCategoryData()
    }

    override fun showLoading() {
        multipleStatusView?.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView?.showContent()
    }

    override fun showCategory(categoryList: ArrayList<CategoryBean>) {
        mCategoryList = categoryList
        mAdapter?.setData(mCategoryList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if(errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView?.showNoNetwork()
        }else {
            multipleStatusView?.showError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}
