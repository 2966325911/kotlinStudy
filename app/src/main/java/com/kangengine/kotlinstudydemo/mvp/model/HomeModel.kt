package com.kangengine.kotlinstudydemo.mvp.model

import android.database.Observable
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils

/**
 *
 *   @author : Vic
 *   time    : 2018-11-18 20:32
 *   desc    :首页精选model
 *
 */
class HomeModel {
    /**
     * 获取首页Banner数据
     */
    fun requestHomeData(num : Int):io.reactivex.Observable<HomeBean> {
        return RetrofitManager.service.getFirstHomeData(num)
            .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多
     */
    fun loadMoreData(url : String):io.reactivex.Observable<HomeBean> {
        return RetrofitManager.service.getMoreHomeData(url)
            .compose(SchedulerUtils.ioToMain())
    }
}