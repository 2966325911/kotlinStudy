package com.kangengine.kotlinstudydemo.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field

/**
 *
 *   @author : Vic
 *   time    : 2018-11-12 15:54
 *   desc    :
 *
 */
object CleanLeakUtils {

    fun fixInputMethodManagerLeak(destContext : Context?) {
        try {
            if(destContext == null) {
                return
            }

            val inputMethodManager = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val viewArray = arrayOf("mCurRootView","mServedVie","mNextServedView")
            var field: Field
            var filedObject : Any?

            for (view in viewArray) {
                field = inputMethodManager.javaClass.getDeclaredField(view)
                if(!field.isAccessible){
                    field.isAccessible = true
                }

                filedObject = field.get(inputMethodManager)
                if(filedObject != null && filedObject is View) {
                    val fileView = filedObject as View?
                    // 被InputMethodManager持有引用的context是想要目标销毁的
                    if(fileView!!.context === destContext) {
                        // 置空，破坏掉path to gc节点
                        field.set(inputMethodManager,null)
                    }else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break
                    }
                }
            }
        } catch (t : Throwable){
            t.printStackTrace()
        }

    }
}