package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.RankContract
import com.kangengine.kotlinstudydemo.mvp.model.RankModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 20:56
 *   desc    :
 *
 */
class RankPresenter : BasePresenter<RankContract.View>(),RankContract.Presenter {
    private val rankModel by lazy {
        RankModel()
    }

    override fun requestRankList(apiUrl: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = rankModel.requestRankList(apiUrl)
            .subscribe({issue->
                mRootView?.apply {
                    dismissLoading()
                    setRanKList(issue.itemList)
                }
            },{ throwable->
                mRootView?.apply {
                    showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }
}