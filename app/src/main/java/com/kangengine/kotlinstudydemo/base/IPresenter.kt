package com.kangengine.kotlinstudydemo.base

/**
 *
 *   @author : Vic
 *   time    : 2018-11-20 21:11
 *   desc    :
 *
 */
interface IPresenter<in V : IBaseView> {

    fun attachView(mRootView: V)
    fun detachView()

}