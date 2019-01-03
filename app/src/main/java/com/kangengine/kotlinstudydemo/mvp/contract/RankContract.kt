package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 20:49
 *   desc    :
 *
 */
class RankContract {
    interface View:IBaseView {
        /**
         * 设置排行榜的数据
         */
        fun setRanKList(itemList: ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取TabInfo
         */
        fun requestRankList(apiUrl : String)
    }
}