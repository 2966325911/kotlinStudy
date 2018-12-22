package com.kangengine.kotlinstudydemo.ui.fragment

import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.mvp.contract.HomeContract
import com.kangengine.kotlinstudydemo.mvp.presenter.HomePresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.HomeAdapter
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import com.scwang.smartrefresh.header.MaterialHeader
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *  首页 每日精选
 */
class HomeFragment : BaseFragment(),HomeContract.View{
    private val TAG : String = HomeFragment::class.java.simpleName
    private val mPresenter by lazy { HomePresenter() }
    private var mTitle: String? =null
    private var num : Int = 1
    private var mHomeAdapter : HomeAdapter? = null
    private var loadingMore = false
    private var isRefresh = false

    private var mMaterialHeader : MaterialHeader? = null
    companion object {
        fun getInstance(title : String) : HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy{
        LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
    }

    private val simpleDataFormat by lazy{
        SimpleDateFormat("-MMM. dd,'Brunch' -", Locale.ENGLISH)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    /**
     * 初始化view
     */
    override fun initView() {
        mPresenter.attachView(this)
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mPresenter.requestHomeData(num)
        }

        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域快背景
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColors(R.color.color_light_black,R.color.color_title_bg)

        mRecyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    val childCount = mRecyclerView.childCount
                    val itemCount = mRecyclerView.layoutManager.itemCount
                    val firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if(firstVisibleItem + childCount == itemCount){
                        if(!loadingMore) {
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if(currentVisibleItemPosition == 0) {
                    //背景设置为透明
                    toolbar.setBackgroundColor(getColor(R.color.color_translucent))
                    iv_search.setImageResource(R.mipmap.ic_action_search_white)
                    tv_header_title.text = ""
                }else {
                    if(mHomeAdapter?.mData!!.size > 1) {
                        toolbar.setBackgroundColor(getColor(R.color.color_title_bg))
                        iv_search.setImageResource(R.mipmap.ic_action_search_black)
                        val itemList = mHomeAdapter!!.mData
                        val item = itemList[currentVisibleItemPosition + mHomeAdapter!!.bannerItemSize-1]
                        if(item.type == "textHeader") {
                            tv_header_title.text = item.data?.text
                        }else {
                            tv_header_title.text = simpleDataFormat.format(item.data?.date)
                        }
                    }
                }
            }
        })

        iv_search.setOnClickListener{openSearchActivity()}

        mLayoutStatusView = multipleStatusView

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let{StatusBarUtil.setPaddingSmart(it,toolbar)}
    }

    private fun openSearchActivity(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = activity?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(it,iv_search,iv_search.transitionName)
            }
        }else {
//            startActivity(Intent(activity,SearchActivity::class.java))
        }
    }


    override fun lazyLoad() {
        mPresenter.requestHomeData(num)
    }

    override fun showLoading() {
        if(!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }

    /**
     * 设置首页的数据
     */
    override fun setHomeData(homeBean: HomeBean) {
        mLayoutStatusView?.showContent()
        mHomeAdapter = activity?.let { HomeAdapter(it,homeBean.issueList[0].itemList) }
        mHomeAdapter?.setBannerSize(homeBean.issueList[0].count)

        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mHomeAdapter?.addItemData(itemList)
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if(errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else{
            mLayoutStatusView?.showError()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    fun getColor(colorId : Int) : Int {
        return resources.getColor(colorId)
    }
}
