package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 21:57
 *   desc    :
 *
 */
interface FollowContract {
    interface View :IBaseView {
        /**
         * 设置关注信息数据
         */
        fun setFollowInfo(issue: HomeBean.Issue)
        fun showError(errorMsg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取list
         */
        fun requestFollowList()
        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}