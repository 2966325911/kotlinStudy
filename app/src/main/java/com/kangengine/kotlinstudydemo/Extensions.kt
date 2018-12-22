package com.kangengine.kotlinstudydemo

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import kotlin.math.min

/**
 *
 *   @author : Vic
 *   time    : 2018-11-25 17:30
 *   desc    :
 *
 */
fun Fragment.showToast(context: String) : Toast{
    val toast = Toast.makeText(this.activity?.applicationContext,context,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun Context.showToast(content:String) : Toast{
    val toast = Toast.makeText(MyApplication.context,content,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun View.pi2dip(pxValue : Float) : Int{
    val scale  = this.resources.displayMetrics.density
    return (pxValue/scale + 0.5).toInt()
}

fun durationFormat(duration:Long?):String{
    val minute = duration !! / 60
    val second = duration % 60
    return if(minute <= 9) {
        if(second <= 9) {
            "0$minute' 0$second''"
        } else {
            "0$minute' $second''"
        }
    }else {
        if(second <= 9) {
            "$minute' 0$second''"
        } else {
            "$minute' $second''"
        }
    }
}

fun Context.dataFormat(total:Long) : String{
    var result : String
    var speedReal : Int = (total/(1024)).toInt()
    result = if(speedReal < 512) {
        speedReal.toString() + "KB"
    } else{
        val mSpeed = speedReal / 1024.0
        (Math.round(mSpeed*100)/100.0).toString() + "MB"
    }
    return result
}