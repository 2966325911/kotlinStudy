package com.kangengine.kotlinstudydemo.net.exception

import android.util.Log
import com.google.gson.JsonParseException
import org.json.JSONException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.logging.Logger

/**
 *
 *   @author : Vic
 *   time    : 2018-11-22 21:29
 *   desc    :
 *
 */
class ExceptionHandle {
    companion object {
        var errorCode = ErrorStatus.UNKNOW_ERROR
        var errorMsg = "请求失败，请稍后重试"
        fun handleException(e : Throwable):String{
            e.printStackTrace()
            if(e is SocketTimeoutException) {
                errorMsg = "网络连接异常"
                errorCode = ErrorStatus.NETWORK_ERROR
            }else if(e is ConnectException) {
                errorMsg = "网络连接异常"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else  if(e is JsonParseException
            || e is JSONException
            || e is ParseException) {
                errorMsg = "数据解析错误"
                errorCode = ErrorStatus.SERVER_ERROR
            } else if(e is ApiException) {
                errorMsg = e.message.toString()
                errorCode = ErrorStatus.SERVER_ERROR
            } else if (e is IllegalArgumentException) {
                errorMsg = "参数错误"
                errorCode = ErrorStatus.SERVER_ERROR
            } else if(e is UnknownHostException){
                errorMsg = "网络连接异常"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else {
                try {
                    com.orhanobut.logger.Logger.e("TAG","错误" + e.message)
                }catch (e1  : Exception) {
                    e1.printStackTrace()
                }
                errorMsg = "未知错误,可能抛锚了吧"
                errorCode = ErrorStatus.UNKNOW_ERROR
            }
            return errorMsg
        }
    }
}