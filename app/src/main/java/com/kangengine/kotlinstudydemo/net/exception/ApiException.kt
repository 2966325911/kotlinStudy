package com.kangengine.kotlinstudydemo.net.exception

import java.lang.RuntimeException

/**
 *
 *   @author : Vic
 *   time    : 2018-11-22 21:27
 *   desc    :
 *
 */
class ApiException : RuntimeException {

    private var code: Int? = null
    constructor(throwable: Throwable,code : Int) : super(throwable) {
        this.code = code
    }
    constructor(message : String) : super(Throwable(message))
}