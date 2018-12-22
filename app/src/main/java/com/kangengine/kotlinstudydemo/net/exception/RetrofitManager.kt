package com.kangengine.kotlinstudydemo.net.exception

import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.api.ApiService
import com.kangengine.kotlinstudydemo.api.UrlConstant
import com.kangengine.kotlinstudydemo.utils.AppUtils
import com.kangengine.kotlinstudydemo.utils.NetworkUtil
import com.kangengine.kotlinstudydemo.utils.Preference
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.sql.Time
import java.util.concurrent.TimeUnit

/**
 *
 *   @author : Vic
 *   time    : 2018-11-22 22:08
 *   desc    :
 *
 */
object RetrofitManager {
    val service : ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiService::class.java)
    }

    private var token:String by Preference("token","")

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor():Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request : Request
            val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("udid"," ")
                .addQueryParameter("deviceModel",AppUtils.getMobileModel())
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头部
     */
    private fun addHeaderInterceptor() : Interceptor{
        return Interceptor { chain: Interceptor.Chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("token", token)
                .method(originalRequest.method(),originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor():Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if(!NetworkUtil.isNetWorkAvailable(MyApplication.context)){
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if(NetworkUtil.isNetWorkAvailable(MyApplication.context)) {
                val maxAge = 0
                //有网络时 设置缓存超时时间为0 意为不读取缓存 只对get起作用
                response.newBuilder()
                    .header("Cache-control","public,max-age="+maxAge)
                    .removeHeader("Retrofit")
                    .build()
            }else {
                //无网络时 设置超时为1天 只对get起作用
                val maxStale = 60 * 60 * 24
                request.newBuilder()
                    .header("Cache-contro","public,max-age="+maxStale)
                    .removeHeader("nyn")
                    .build()
            }
            response
        }
    }
    private fun getRetrofit() : Retrofit {
         return Retrofit.Builder()
             .baseUrl(UrlConstant.BASE_URL)
             .client(getOkHttpClient())
             .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
             .addConverterFactory(GsonConverterFactory.create())
             .build()
    }

    private fun getOkHttpClient() : OkHttpClient {
        //添加日志拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //设置请求缓存的大小跟位置
        val cacheFile = File(MyApplication.context.cacheDir,"cache")
        //缓存大小 50M
        val cache = Cache(cacheFile,1024*1024*50)

        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor())
            .addInterceptor(addHeaderInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60L,TimeUnit.SECONDS)
            .readTimeout(60L,TimeUnit.SECONDS)
            .writeTimeout(60L,TimeUnit.SECONDS)
            .build();
    }
}