package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-25 21:56
 *   desc    :
 *
 */
interface CategoryContract {
    interface View : IBaseView {
        /**
         * 显示分类的信息
         */
        fun showCategory(categoryList : ArrayList<CategoryBean>)
        /**
         * 显示错误信息
         */
        fun showError(errorMsg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取分类的信息
         */
        fun getCategoryData()
    }
}