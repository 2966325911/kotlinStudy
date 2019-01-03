package com.kangengine.kotlinstudydemo.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean
import com.kangengine.kotlinstudydemo.net.exception.RetrofitManager
import com.kangengine.kotlinstudydemo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 18:17
 *   desc    :
 *
 */
class HotTabModel {
    /**
     * 获取TabInfo
     */
    fun getTabInfo() : Observable<TabInfoBean> {
        return RetrofitManager.service.getRandList()
            .compose(SchedulerUtils.ioToMain())
    }

}