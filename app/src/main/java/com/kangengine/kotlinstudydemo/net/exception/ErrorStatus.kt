package com.kangengine.kotlinstudydemo.net.exception

/**
 *
 *   @author : Vic
 *   time    : 2018-11-22 21:29
 *   desc    :
 *
 */
object ErrorStatus {

    /**
     * 响应成功
     */
    @JvmField
    val SUCCESS = 0

    /**
     * 未知错误
     */
    @JvmField
    val UNKNOW_ERROR = 1002

    /**
     * 服务器内部错误
     */
    @JvmField
    val SERVER_ERROR = 1003
    /**
     * 网络连接超时
     */
    @JvmField
    val NETWORK_ERROR = 1004

    /**
     * API解析异常
     */
    @JvmField
    val API_ERROR = 1005
}