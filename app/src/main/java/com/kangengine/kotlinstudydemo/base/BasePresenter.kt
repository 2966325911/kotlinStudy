package com.kangengine.kotlinstudydemo.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.RuntimeException

/**
 *
 *   @author : Vic
 *   time    : 2018-11-20 21:13
 *   desc    :
 *
 */
open class BasePresenter<T : IBaseView> : IPresenter<T> {
    var mRootView : T? = null
    private set

    private var compositeDisposable = CompositeDisposable()


    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    override fun detachView() {
        mRootView = null
        if(!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    private val isViewAttached : Boolean
        get() = mRootView != null

    fun checkViewAttached(){
        if(!isViewAttached) throw MvpViewNotAttachedException()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private class MvpViewNotAttachedException internal constructor() :RuntimeException(
        "Please call IPresenter.attachView(IBaseView) before" + "requesting data to" +
                "the IPresenter"
    )
}