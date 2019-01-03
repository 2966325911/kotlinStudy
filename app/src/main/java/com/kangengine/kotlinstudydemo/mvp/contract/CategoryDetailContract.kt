package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-26 21:38
 *   desc    :
 *
 */
interface CategoryDetailContract {
    interface View : IBaseView {
        /**
         * 设置列表数据
         */
        fun setCateDetailList(itemList : ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg : String)
    }

    interface Presenter : IPresenter<View> {
        fun getCategoryDetailList(id:Long)
        fun loadMoreData()
    }
}