package com.kangengine.kotlinstudydemo.api

import com.hazz.kotlinmvp.mvp.model.bean.AuthorInfoBean
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import kotlin.collections.ArrayList

/**
 *
 *   @author : Vic
 *   time    : 2018-11-18 20:28
 *   desc    : api接口
 *
 */
interface ApiService {
    /**
     * 首页精选
     */
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num:Int): Observable<HomeBean>

    /**
     * 根据nextPageUrl 去请求一页数据
     */
    @GET
    fun getMoreHomeData(@Url url : String) : Observable<HomeBean>

    /**
     * 根据item id获取相关视频
     */
    @GET("v4/video/related?")
    fun getRelatedData(@Query("id") id:Long):Observable<HomeBean.Issue>

    /**
     * 获取分类
     */
    @GET("v4/categories")
    fun getCategory():Observable<ArrayList<CategoryBean>>

    /**
     * 获取分类详情list
     */
    @GET("v4/categories/videoList?")
    fun getCategoryDetailList(@Query("id")id : Long):Observable<HomeBean.Issue>

    /**
     * 获取更多的Issue
     */
    @GET
    fun getIssusData(@Url url: String):Observable<HomeBean.Issue>

    /**
     * 获取全部排行旁的Info
     */
    @GET("v4/rankList")
    fun getRandList():Observable<TabInfoBean>

    /**
     * 获取搜索信息
     */
    @GET("v1/search?&num=10&start=10")
    fun getSearchData(@Query("query") query:String):Observable<HomeBean.Issue>

    /**
     * 热门搜索词
     */
    @GET("v3/queries/hot")
    fun getHotWord():Observable<ArrayList<String>>

    /**
     * 关注
     */
    @GET("v4/tabs/follow")
    fun getFollowInfo():Observable<HomeBean.Issue>

    @GET("v4/pacs/detail/tab?")
    fun getAuthorInfo(@Query("id")id:Long):Observable<AuthorInfoBean>
}