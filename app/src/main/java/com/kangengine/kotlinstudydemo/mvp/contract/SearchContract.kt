package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-22 11:56
 *   desc    : 搜索契约类
 *
 */
interface SearchContract {
    interface  View : IBaseView {
        /**
         * 设置热门关键词数据
         */
        fun setHotWordData(string:ArrayList<String>)
        /**
         * 设置搜索关键词返回的结果
         */
        fun setSearchResult(issue: HomeBean.Issue)
        /**
         * 关闭软键盘
         */
        fun closeSoftKeyboard()
        /**
         * 设置空view
         */
        fun setEmptyView()

        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter : IPresenter<View>{
        /**
         * 获取热门关键字的数据
         */
        fun requestHotWordData()
        /**
         *  查询搜索
         */
        fun querySearchData(words:String)
        /**
         * 加载更多
         */
         fun loadMoreData()
    }
}
