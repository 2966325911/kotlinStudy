package com.kangengine.kotlinstudydemo.view.recyclerview

import java.text.FieldPosition

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 21:16
 *   desc    :
 *
 */
interface MultipleType<in T>{
    fun getLayoutId(item : T,position: Int) : Int
}