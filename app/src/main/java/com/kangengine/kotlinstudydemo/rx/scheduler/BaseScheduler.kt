package com.kangengine.kotlinstudydemo.rx.scheduler

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 19:47
 *   desc    :
 *
 */
abstract class BaseScheduler<T> protected constructor(private val subscribeOnScheduler : Scheduler,
                                                      private val observerOnScheduler:Scheduler) : ObservableTransformer<T,T>,
        SingleTransformer<T,T>,
        MaybeTransformer<T,T>,
        CompletableTransformer,
        FlowableTransformer<T,T>{
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(subscribeOnScheduler)
            .observeOn(observerOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
            .observeOn(observerOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
            .observeOn(observerOnScheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
            .observeOn(observerOnScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
            .observeOn(observerOnScheduler)
    }
}