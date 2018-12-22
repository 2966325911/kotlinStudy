package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 19:39
 *   desc    : Home 契约类
 */
interface HomeContract {
    interface View : IBaseView {
        /**
         * 设置第一次请求的数据
         */
        fun setHomeData(homeBean: HomeBean)

        /**
         * 设置加载更过数据
         */
        fun  setMoreData(itemList : ArrayList<HomeBean.Issue.Item>)

        /**
         * 显示错误信息
         */
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        /**
         * 获取首页精选数据
         */
        fun requestHomeData(num : Int)

        /**
         * 加载更多数据
         */
        fun loadMoreData()
    }
}