package com.kangengine.kotlinstudydemo.mvp.contract

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.base.IBaseView
import com.kangengine.kotlinstudydemo.base.IPresenter

/**
 *
 *   @author : Vic
 *   time    : 2018-12-16 16:09
 *   desc    :
 *
 */
interface VideoDetailContract {

    interface View : IBaseView {
        /**
         * 设置视频播放资源
         */
        fun setVideo(url:String)

        /**
         * 设置视频信息
         */
        fun setVideoInfo(itemInfo:HomeBean.Issue.Item)

        /**
         * 设置背景
         */
        fun setBackground(url: String);

        /**
         * 设置最新相关视频
         */
        fun setRecentRelatedVideo(itemList : ArrayList<HomeBean.Issue.Item>)

        /**
         * 设置错误信息
         */
        fun setErrorMsg(errMsg : String);
    }

    interface Presenter : IPresenter<View> {
        /**
         * 加载视频信息
         */
        fun loadVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 请求相关的视频信息
         */
        fun requestRelatedVideo(id:Long)
    }
}