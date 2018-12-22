package com.kangengine.kotlinstudydemo.mvp.presenter

import android.app.Activity
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.dataFormat
import com.kangengine.kotlinstudydemo.mvp.contract.VideoDetailContract
import com.kangengine.kotlinstudydemo.mvp.model.VideoDetailMode
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.utils.DisplayManager
import com.kangengine.kotlinstudydemo.utils.NetworkUtil

/**
 *
 *   @author : Vic
 *   time    : 2018-12-16 16:20
 *   desc    :
 *
 */
class VideoDetailPresenter : BasePresenter<VideoDetailContract.View>(),VideoDetailContract.Presenter {

    private val videoDetailModel : VideoDetailMode by lazy {
        VideoDetailMode()
    }

    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {
        val playInfo = itemInfo.data?.playInfo

        val netType = NetworkUtil.isWifi(MyApplication.context)

        checkViewAttached()
        if(playInfo!!.size > 1) {
            //当前网络是WiFi环境则选择高清的视频
            if(netType){
                for(i in playInfo) {
                    if(i.type == "high") {
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        break
                    }

                }
            } else {
                //选择标清的视频
                for(i in  playInfo) {
                    if(i.type == "normal"){
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)

                        (mRootView as Activity).showToast("本次消耗${(mRootView as Activity)
                            .dataFormat(i.urlList[0].size)}流量")
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideo(itemInfo.data.playUrl)
        }

        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!!-
        DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)
    }

    override fun requestRelatedVideo(id: Long) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.requestRelatedData(id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    setRecentRelatedVideo(issue.itemList)
                }
            },{t->
                mRootView?.apply {
                    dismissLoading()
                    setErrorMsg(ExceptionHandle.handleException(t))
                }
            })

        addSubscription(disposable)
    }
}