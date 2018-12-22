package com.kangengine.kotlinstudydemo.rx.scheduler

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 19:46
 *   desc    :
 *
 */
class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(),
    AndroidSchedulers.mainThread())