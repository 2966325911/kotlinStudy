package com.kangengine.kotlinstudydemo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import okio.Timeout

/**
 *
 *   @author : Vic
 *   time    : 2018-11-17 16:52
 *   desc    : 网络工具类
 *
 */
class NetworkUtil {
    companion object {
        //NetworkAvailable
        var NET_CNNT_BAIDU_OK = 1
        //no NetworkAvailable
        var NET_CNNT_BAIDU_TIMEOUT = 2
        //Net no ready
        var NET_NOT_PREPARE = 3
        // net error
        var NET_ERROR = 4
        private val TIMEOUT = 3000

        /**
         *  check NetworkAvailable
         */
        @JvmStatic
        fun isNetWorkAvailable(context: Context) : Boolean {
            val manager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return !(null == info || !info.isAvailable)
        }

        /**
         * 是否是手机网络
         * @JvmStatic标识静态方法
         */
        @JvmStatic
        fun isMobileNet(context: Context) : Boolean{
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
        }

        /**
         * isWifi
         */
        @JvmStatic
        fun isWifi(context: Context) : Boolean{
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * wifi is on
         */
        fun isWifiEnabled(context: Context) : Boolean{
            val mgrConn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as  ConnectivityManager

            val mgrTel = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            return mgrConn.activeNetworkInfo != null && mgrConn
                .activeNetworkInfo.state == NetworkInfo.State.CONNECTED || mgrTel
                .networkType == TelephonyManager.NETWORK_TYPE_UMTS
        }
    }
}