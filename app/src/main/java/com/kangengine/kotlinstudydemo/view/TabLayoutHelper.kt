package com.kangengine.kotlinstudydemo.view

import android.annotation.SuppressLint
import android.os.Build
import android.support.design.widget.TabLayout
import android.widget.LinearLayout
import com.kangengine.kotlinstudydemo.utils.DisplayManager
import java.lang.Exception
import java.lang.reflect.Field

/**
 *
 *   @author : Vic
 *   time    : 2018-12-24 21:19
 *   desc    :
 *
 */
object TabLayoutHelper {
    @SuppressLint("ObsoleteSdkInt")
    fun setUpIndicatorWidth(tabLayout: TabLayout) {
        val tabLayoutClass = tabLayout.javaClass
        var tabStrip : Field? = null

        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip")
            tabStrip!!.isAccessible = true

            var layout : LinearLayout? = null
            if(tabStrip != null) {
                layout = tabStrip.get(tabLayout) as LinearLayout
            }

            for(i in 0 until  layout!!.childCount) {
                val child = layout.getChildAt(i)
                child.setPadding(0,0,0,0)
                val params = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1f)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.marginStart = DisplayManager.dip2px(50f)!!
                    params.marginEnd = DisplayManager.dip2px(50f)!!
                }

                child.layoutParams = params
                child.invalidate()
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }


    }
}