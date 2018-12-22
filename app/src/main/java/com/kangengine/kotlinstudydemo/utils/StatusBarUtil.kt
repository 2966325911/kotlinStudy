package com.kangengine.kotlinstudydemo.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.FloatRange
import android.support.annotation.RequiresApi
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import java.lang.Exception
import java.util.regex.Pattern

/**
 *
 *   @author : Vic
 *   time    : 2018-11-18 17:18
 *   desc    :
 *
 */
class StatusBarUtil {
    companion object {
        private var DEFAULT_COLOR = 0
        private var DEFAULT_ALPHA = 0f
        private val MIN_API = 19

        /**
         * 判断是否是Flyme4以上
         */
        private val isFlyme4Later : Boolean
            get() = (Build.FINGERPRINT.contains("Flyme_OS_4")
                    || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                    || Pattern.compile("Flyme OS [4|5]",Pattern.CASE_INSENSITIVE)
                .matcher(Build.DISPLAY).find())

        /**
         * 判断是否是miui6以上
         */
        private val isMIUI6Later : Boolean
            get() {
                return try {
                    val clz = Class.forName("android.os.SystemProperties")
                    val mtd = clz.getMethod("get",String::class.java)
                    var miui = mtd.invoke(null,"ro.miui.ui.version.name") as String
                    miui = miui.replace("[vV]".toRegex(),"")
                    val version = Integer.parseInt(miui)
                    version > 6

                }catch (e : Exception) {
                    false
                }
            }

        @JvmOverloads
        fun immersive(activity: Activity,color:Int = DEFAULT_COLOR,
                      @FloatRange(from = 0.0,to = 1.0) alpha:Float = DEFAULT_ALPHA){
            immersive(activity.window,color,alpha)
        }

        fun immersive(window:Window){
            immersive(window, DEFAULT_COLOR, DEFAULT_ALPHA)
        }

        fun immersive(window: Window,color: Int,@FloatRange(from = 0.0,to = 1.0) alpha:Float=1f){
            if(Build.VERSION.SDK_INT > 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = mixtureColor(color,alpha)

                var systemUiVisibility = window.decorView.systemUiVisibility
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.decorView.systemUiVisibility = systemUiVisibility
            }else if(Build.VERSION.SDK_INT >= 19) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                setTranslucentView(window.decorView as ViewGroup,color,alpha)
            } else if(Build.VERSION.SDK_INT >= MIN_API && Build.VERSION.SDK_INT > 19) {
                var systemUIVisilibility = window.decorView.systemUiVisibility
                systemUIVisilibility = systemUIVisilibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                systemUIVisilibility = systemUIVisilibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.decorView.systemUiVisibility = systemUIVisilibility
            }
        }

        fun darkMode(activity: Activity,dark : Boolean) {
            when{
                isFlyme4Later-> darkmModeFlyme4(activity.window,dark)
                isMIUI6Later ->darkModeForMIUI6(activity.window,dark)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M->darkModeForM(activity.window,dark)
            }
        }

        /**
         * 设置状态栏darkMode，字体颜色以及icon变黑(支持MIUI6以上，Flyme4以上，Android M 以上)
         */

        fun darkMode(activity: Activity) {
            darkMode(activity.window, DEFAULT_COLOR, DEFAULT_ALPHA)
        }

        fun darkMode(activity: Activity,color : Int,@FloatRange(from = 0.0,to = 1.0) alpha: Float) {
            darkMode(activity.window, color, alpha)
        }

        fun darkMode(window: Window,color : Int,@FloatRange(from = 0.0,to = 1.0)
                     alpha: Float) {
            when{
                isFlyme4Later->{
                    darkmModeFlyme4(window,true)
                    immersive(window,color,alpha)
                }
                isMIUI6Later->{
                    darkModeForMIUI6(window,true)
                    immersive(window,color,alpha)
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M->{
                    darkModeForM(window,true)
                    immersive(window,color,alpha)
                }
                Build.VERSION.SDK_INT >= 19->{
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    setTranslucentView(window.decorView as ViewGroup,color,alpha)
                }
                else-> immersive(window,color,alpha)
            }

        }

        /**
         * android6.0设置字体颜色
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun darkModeForM(window: Window,dark : Boolean) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = if(dark) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            window.decorView.systemUiVisibility = systemUiVisibility
        }

        fun darkmModeFlyme4(window: Window?,dark:Boolean):Boolean{
            var result = false
            if(window != null) {
                try{
                    val e = window.attributes
                    val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                    val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                    darkFlag.isAccessible = true
                    meizuFlags.isAccessible = true
                    val bit = darkFlag.getInt(null)
                    var value = meizuFlags.getInt(e)
                    if(dark) {
                        value = value or bit
                     }else {
                        value = value and bit.inv()
                    }

                    meizuFlags.setInt(e,value)
                    window.attributes = e
                    result = true
                }catch (var8 : Exception) {

                }
            }
            return result
        }

        /**
         * 设置MIUI6+的状态栏是否为darkMode,darkMode的字体颜色及icon变黑
         */
        fun darkModeForMIUI6(window: Window,darkmode:Boolean):Boolean{
            val clazz = window.javaClass
            return try {
                val darkModeFlag:Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags",Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType)
                extraFlagField.invoke(window,if(darkmode) darkModeFlag else 0,darkModeFlag)
                true
            }catch (e : Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 增加view的paddingTop，增加的值为状态栏高度
         */
        fun setPadding(context: Context,view: View) {
            if(Build.VERSION.SDK_INT >= MIN_API) {
                view.setPadding(view.paddingLeft,view.paddingTop +
                getStatusBarHeight(context),view.paddingRight,view.paddingBottom)
            }
        }

        /**
         * 增加view的paddingTop，自动判断高度
         */
        fun setPaddingSmart(context: Context,view: View) {
            if(Build.VERSION.SDK_INT >= MIN_API) {
                val lp = view.layoutParams
                if(lp != null && lp.height > 0) {
                    lp.height += getStatusBarHeight(context)
                }
                view.setPadding(view.paddingLeft,view.paddingTop +
                        getStatusBarHeight(context),view.paddingRight,view.paddingBottom)
            }
        }

        fun setHeightAndPadding(context: Context,view: View) {
            if(Build.VERSION.SDK_INT >= MIN_API) {
                val lp = view.layoutParams
                lp.height += getStatusBarHeight(context)
                view.setPadding(view.paddingLeft,view.paddingTop+ getStatusBarHeight(context)
                ,view.paddingRight,view.paddingBottom)
            }
        }

        fun setMargin(context: Context,view: View) {
            if(Build.VERSION.SDK_INT >= MIN_API) {
                val lp = view.layoutParams
                if(lp is ViewGroup.MarginLayoutParams){
                    lp.topMargin += getStatusBarHeight(context)
                }
                view.layoutParams = lp
            }
        }

        /**
         *创建假的透明状态栏
         */

        fun setTranslucentView(container : ViewGroup,color: Int,@FloatRange(from = 0.0,to = 1.0)
        alpha: Float) {
            if(Build.VERSION.SDK_INT >= 19) {
                val mixtureColor = mixtureColor(color,alpha)
                var translucentView : View? = container.findViewById(android.R.id.custom)
                if(translucentView == null && mixtureColor != 0) {
                    translucentView = View(container.context)
                    translucentView.id = android.R.id.custom
                    val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(container.context))
                    container.addView(translucentView,lp)
                }
                if(translucentView != null) {
                    translucentView.setBackgroundColor(mixtureColor)
                }
            }
        }

        fun mixtureColor(color: Int,@FloatRange(from = 0.0,to = 1.0) alpha: Float):Int{
            val a = if(color and -0x1000000 == 0) 0xff else color.ushr(24)
            return color and 0x00ffffff or ((a*alpha).toInt() shl 24)
        }

        fun getStatusBarHeight(context: Context) : Int{
            var result = 24
            var resId = context.resources.getIdentifier("status_bar_height","dimen",
                "android")
            result = if(resId > 0) {
                context.resources.getDimensionPixelSize(resId)
            }else {
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result.toFloat(),Resources.getSystem().displayMetrics).toInt()
            }
            return result
        }
    }


}