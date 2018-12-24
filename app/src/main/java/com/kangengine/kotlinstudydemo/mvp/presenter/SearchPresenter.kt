package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.SearchContract
import com.kangengine.kotlinstudydemo.mvp.model.SearchModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle
import com.tencent.bugly.proguard.t

/**
 *
 *   @author : Vic
 *   time    : 2018-12-22 12:03
 *   desc    :
 *
 */
class SearchPresenter : BasePresenter<SearchContract.View>(),SearchContract.Presenter {
    private var nextPageUrl : String?= null
    private val searchModel by lazy {
        SearchModel()
    }

    /**
     * 获取热门关键词
     */
    override fun requestHotWordData() {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }

       addSubscription(disposable = searchModel.requestHotWordData()
           .subscribe({string->
               mRootView?.apply {
                   setHotWordData(string)
               }
           },{throwable ->
               mRootView?.apply {
                   showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
               }
           }))
    }


    /**
     * 查询关键词
     */
    override fun querySearchData(words: String) {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }

        addSubscription(disposable = searchModel.getSearchResult(words)
            .subscribe({issue->
                mRootView?.apply {
                    dismissLoading()
                    if(issue.count > 0&& issue.itemList.size > 0) {
                        nextPageUrl = issue.nextPageUrl
                        setSearchResult(issue)
                    }else{
                        setEmptyView()
                    }
                }
            },{throwable->
                mRootView?.apply {
                    dismissLoading()
                    showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                }
            }))
    }

    /**
     * 加载更多数据
     */
    override fun loadMoreData() {
        checkViewAttached()
        nextPageUrl?.let {
            addSubscription(disposable = searchModel.loadModeData(it)
                .subscribe({issue ->
                    mRootView?.apply {
                        nextPageUrl = issue.nextPageUrl
                        setSearchResult(issue)
                    }
                },{throwable->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                    }
                }))
        }
    }

}