package com.kangengine.kotlinstudydemo.net

/**
 *
 *   @author : Vic
 *   time    : 2018-11-22 21:24
 *   desc    :封装返回的数据
 *
 */
class BaseResponse<T> (val code : Int,val msg:String,val data:T)