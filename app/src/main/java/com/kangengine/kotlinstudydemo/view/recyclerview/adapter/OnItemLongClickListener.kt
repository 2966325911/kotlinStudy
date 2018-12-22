package com.kangengine.kotlinstudydemo.view.recyclerview.adapter


/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 21:37
 *   desc    :
 *
 */
interface OnItemLongClickListener {
    fun onItemLongClick(obj:Any?,position: Int):Boolean
}