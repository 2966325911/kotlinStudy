package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.FollowContract
import com.kangengine.kotlinstudydemo.mvp.model.FollowModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 22:09
 *   desc    :
 *
 */
class FollowPresenter : BasePresenter<FollowContract.View>(),FollowContract.Presenter {

    private val followModel by lazy { FollowModel() }

    private var nextPageUrl : String?= null

    /**
     * 请求关注数据
     */
    override fun requestFollowList() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = followModel.requestFollowList()
            .subscribe({issue->
                mRootView?.apply {
                    dismissLoading()
                    nextPageUrl = issue.nextPageUrl
                    setFollowInfo(issue)
                }
            },{throwable->
                mRootView?.apply {
                    //处理异常
                    showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }


    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            followModel.loadModeData(it)
                .subscribe({issue->
                    mRootView?.apply {
                        nextPageUrl = issue.nextPageUrl
                        setFollowInfo(issue)
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