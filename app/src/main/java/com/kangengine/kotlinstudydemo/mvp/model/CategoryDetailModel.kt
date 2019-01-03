package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-26 21:42
 *   desc    :
 *
 */
class CategoryDetailModel {
    /**
     * 获取下拉的List数据
     */
    fun getCategoryDetailList(id:Long) : Observable<HomeBean.Issue> {
        return RetrofitManager.service.getCategoryDetailList(id)
            .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData(url : String) : Observable<HomeBean.Issue> {
        return RetrofitManager.service.getIssusData(url)
            .compose(SchedulerUtils.ioToMain())
    }
}