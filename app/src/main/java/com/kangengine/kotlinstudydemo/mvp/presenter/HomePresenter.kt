package com.kangengine.kotlinstudydemo.mvp.presenter

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.HomeContract
import com.kangengine.kotlinstudydemo.mvp.model.HomeModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 20:04
 *   desc    :
 *
 */
class HomePresenter : BasePresenter<HomeContract.View>(),HomeContract.Presenter {
    private var bannerHomeBean : HomeBean? = null
    private var nextPageUrl : String? = null
    private val homeModel by lazy {
        HomeModel()
    }

    /**
     * 获取首页精选数据banner 数据
     */
    override fun requestHomeData(num: Int) {
        //检测是否绑定
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeModel
            .requestHomeData(num)
            .flatMap({homeBean->
                val bannerItemList = homeBean.issueList[0].itemList

                bannerItemList.filter {item ->
                    item.type == "bannder2" || item.type == "horizontalScrollCard"
                }.forEach { item ->
                    bannerItemList.remove(item)
                }
                //记录第一页数据当做是banner数据
                bannerHomeBean = homeBean
                //根据nextPageURL请求下一页数据
                homeModel.loadMoreData(homeBean.nextPageUrl)
            }).subscribe({homBean->
                mRootView?.apply {
                    dismissLoading()
                    nextPageUrl = homBean.nextPageUrl

                    //过滤掉Banner2 (包含广告 等不需要的Type)
                    val newBannerItemList = homBean.issueList[0].itemList

                    newBannerItemList.filter { item ->
                        item.type == "banner2" || item.type == "horizontalScrollCard"
                    }.forEach { item ->
                        newBannerItemList.remove(item)
                    }
                    //重新复制Banner长度
                    bannerHomeBean!!.issueList[0].count = bannerHomeBean!!.issueList[0].itemList.size
                    //赋值过滤后的数据
                    bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)

                    setHomeData(bannerHomeBean!!)
                }
            },{t->
                mRootView?.apply {
                    dismissLoading()
                    showError(ExceptionHandle.handleException(t),
                        ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            homeModel.loadMoreData(it)
                .subscribe({homeBean->
                    mRootView?.apply {
                        val newItemList = homeBean.issueList[0].itemList
                        newItemList.filter { item ->
                            item.type == "banner2" || item.type == "horizontalScrollCard"
                        }.forEach { item->
                            newItemList.remove(item)
                        }
                        nextPageUrl = homeBean.nextPageUrl
                        setMoreData(newItemList)
                    }
                },{t->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }
                })
        }
        if(disposable != null) {
            addSubscription(disposable)
        }
    }
}