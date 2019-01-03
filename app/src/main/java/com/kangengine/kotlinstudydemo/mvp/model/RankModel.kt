package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 20:54
 *   desc    :
 *
 */
class RankModel {
    /**
     * 获取排行榜
     */
    fun requestRankList(apiUrl:String) : Observable<HomeBean.Issue> {
        return RetrofitManager.service.getIssusData(apiUrl)
            .compose(SchedulerUtils.ioToMain())
    }
}