package com.kangengine.kotlinstudydemo.rx.scheduler

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 19:56
 *   desc    :
 *
 */
object SchedulerUtils {
    fun <T> ioToMain() : IoMainScheduler<T>{
        return IoMainScheduler()
    }
}