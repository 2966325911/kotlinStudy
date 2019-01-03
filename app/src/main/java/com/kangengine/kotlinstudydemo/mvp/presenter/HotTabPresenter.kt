package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.HotTabContract
import com.kangengine.kotlinstudydemo.mvp.model.HotTabModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 18:19
 *   desc    :
 *
 */
class HotTabPresenter : BasePresenter<HotTabContract.View>(),HotTabContract.Presenter {
    private val hotTabModel by lazy {
        HotTabModel()
    }

    override fun getTabInfo() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = hotTabModel.getTabInfo()
            .subscribe({
                tabInfo->
                mRootView?.setTableInfo(tabInfo)
            },{
                throwable->
                mRootView?.showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
            })
        addSubscription(disposable)
    }
}