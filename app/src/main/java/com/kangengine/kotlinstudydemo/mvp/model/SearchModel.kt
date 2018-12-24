package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-22 12:07
 *   desc    : 搜索model
 *
 */
class SearchModel {
    /**
     * 请求热门关键词
     */
    fun requestHotWordData(): Observable<ArrayList<String>>{
        return RetrofitManager.service.getHotWord()
            .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 搜索关键词返回的结果
     */
     fun getSearchResult(words:String):Observable<HomeBean.Issue> {
        return RetrofitManager.service.getSearchData(words)
            .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多数据
     */
    fun loadModeData(url:String):Observable<HomeBean.Issue> {
        return RetrofitManager.service.getIssusData(url)
            .compose(SchedulerUtils.ioToMain())
    }


}