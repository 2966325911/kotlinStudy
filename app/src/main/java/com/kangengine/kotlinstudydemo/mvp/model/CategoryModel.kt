package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-25 21:59
 *   desc    :
 *
 */
class CategoryModel {
    /**
     * 获取信息分类
     */
    fun getCategoryData():Observable<ArrayList<CategoryBean>> {
        return RetrofitManager.service.getCategory()
            .compose(SchedulerUtils.ioToMain())
    }
}