package com.kangengine.kotlinstudydemo.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 *
 *   @author : Vic
 *   time    : 2018-11-12 15:30
 *   desc    :
 *
 */
object DisplayManager {
    init {

    }
    private var displayMetrics : DisplayMetrics?= null
    private var screenWidth : Int? = null
    private var screenHeight: Int? = null
    private var screenDpi: Int? = null

    fun init(context : Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }

    //UI图的大小
    private const val STANDARD_WIDTH = 1080
    private const val STANDARD_HEIGHT = 1920

    fun getScreenWidth(): Int?{
        return screenWidth
    }

    fun  getScreenHeight(): Int?{
        return screenHeight
    }

    fun getPaintSize(size : Int) : Int?{
        return getRealHeight(size)
    }

    fun getRealWidth(px : Int) : Int ?{
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     * @return
     */
    fun getRealWidth(px : Int,parentWidth : Float) : Int?{
        return (px/parentWidth * getScreenWidth()!!).toInt()
    }


    fun getRealHeight(px : Int) : Int?{
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     *  输入UI图的尺寸，输出实际的px，第二参数是父布局
     *  @param px  UI图的实际大小
     *  @param parentHeight 父view在ui途中的高度
     */
    fun getRealHeight(px : Int,parentHeight : Float) : Int?{
        return (px/parentHeight* getScreenHeight()!!).toInt()
    }

    /**
     * dip转px
     */
    fun dip2px(dipValue: Float): Int{
        val scale = displayMetrics?.density
        return (dipValue * scale!! + 0.5f).toInt()
    }


}