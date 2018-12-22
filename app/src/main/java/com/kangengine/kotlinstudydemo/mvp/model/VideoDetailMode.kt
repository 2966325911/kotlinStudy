package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-16 16:18
 *   desc    :
 *
 */
class VideoDetailMode {

    fun requestRelatedData(id:Long):Observable<HomeBean.Issue>{
        return RetrofitManager.service.getRelatedData(id)
            .compose(SchedulerUtils.ioToMain())
    }
}