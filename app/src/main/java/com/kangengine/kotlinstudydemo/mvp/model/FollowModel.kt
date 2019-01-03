package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 22:02
 *   desc    :
 *
 */
class FollowModel {

    /**
     * 获取关注信息
     */
    fun requestFollowList():Observable<HomeBean.Issue> {
        return RetrofitManager.service.getFollowInfo()
            .compose(SchedulerUtils.ioToMain())
    }

    fun loadModeData(url:String):Observable<HomeBean.Issue> {
        return RetrofitManager.service.getIssusData(url)
            .compose(SchedulerUtils.ioToMain())
    }


}