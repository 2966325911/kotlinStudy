package com.kangengine.kotlinstudydemo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.kangengine.kotlinstudydemo.utils.DisplayManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

/**
 *
 *   @author : Vic
 *   time    : 2018-11-12 14:41
 *   desc    :
 *
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {


    private var refWatcher : RefWatcher? = null

    companion object {
        private val TAG = "MyApplication"

        var context : Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context) : RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }

    override fun onCreate(){
        super.onCreate()
        context = applicationContext
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(this)
    }


    private fun initConfig(){
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("kotlinStudy")
            .build()
        Logger.addLogAdapter(object  : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

}