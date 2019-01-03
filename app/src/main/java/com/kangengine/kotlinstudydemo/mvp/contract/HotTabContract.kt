package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 18:14
 *   desc    :
 *
 */
interface HotTabContract {

    interface  View:IBaseView {
        /**
         *  设置TabInfo
         */
        fun setTableInfo(tableInfoBean : TabInfoBean)
        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter:IPresenter<View> {
        /**
         * 获取TabInfo
         */
        fun getTabInfo()
    }
}