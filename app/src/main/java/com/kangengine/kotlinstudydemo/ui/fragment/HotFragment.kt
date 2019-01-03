package com.kangengine.kotlinstudydemo.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.base.BaseFragmentAdapter
import com.kangengine.kotlinstudydemo.mvp.contract.HotTabContract
import com.kangengine.kotlinstudydemo.mvp.presenter.HotTabPresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_hot.*


class HotFragment : BaseFragment(),HotTabContract.View {

    private val mPresenter by lazy {
        HotTabPresenter()
    }

    private var mTitle : String? = null
    /**
     * 存放tab标题
     */
    private val mTabTitleList = ArrayList<String>()
    private val mFragmentList = ArrayList<Fragment>()

    companion object {
        fun getInstance(title : String) : HotFragment{
            val fragment = HotFragment()
            fragment.arguments = Bundle()
            fragment.mTitle = title
            return fragment
        }
    }

    init {
        mPresenter.attachView(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_hot


    override fun lazyLoad() {
        mPresenter.getTabInfo()
    }


    override fun initView() {
        mLayoutStatusView = multipleStatusView
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it,toolbar) }
    }


    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {

    }

    override fun setTableInfo(tableInfoBean: TabInfoBean) {
        multipleStatusView.showContent()

        tableInfoBean.tabInfo.tabList.mapTo(mTabTitleList){it.name}
        tableInfoBean.tabInfo.tabList.mapTo(mFragmentList){
            RankFragment.getInstance(it.apiUrl)
        }

        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager,mFragmentList,mTabTitleList)
        mTabLayout.setupWithViewPager(mViewPager)
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
