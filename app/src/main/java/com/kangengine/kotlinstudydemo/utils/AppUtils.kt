package com.kangengine.kotlinstudydemo.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

/**
 *
 *   @author : Vic
 *   time    : 2018-11-17 16:08
 *   desc    :
 * private constructor 私有构造方法
 */
class AppUtils private constructor(){
    //init {}初始化代码块
    init {
        throw Error("Do not need instantiate")
    }

    //伴生对象
    companion object {
        private val TAG = "AppUtils"

        /**
         * 得到软件版本号
         */
        fun getVerCode(context : Context):Int {
            var verCode = -1
            try{
                val packageName = context.packageName
                verCode = context.packageManager.getPackageInfo(packageName,0).versionCode
            }catch ( e : PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verCode
        }

        /**
         * 获取应用运行的最大内存
         */
        val maxMemory : Long
            get() = Runtime.getRuntime().maxMemory() / 1024

        /**
         * 获取应用的版本信息
         */
        fun getVerName(context: Context): String{
            var verName = ""
            try {
                val packageName = context.packageName
                verName = context.packageManager.getPackageInfo(packageName,0).versionName
            }catch (e : PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }


            return verName
        }

        @SuppressLint("PackageManagerGetSignatures")
                /**
         * 获取应用的签名
         */

        fun getSign(context: Context,pkgName : String):String? {

            return try {
                val pis = context.packageManager.getPackageInfo(pkgName,PackageManager.GET_SIGNATURES)
                hexDigest(pis.signatures[0].toByteArray())
            }catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        /**
         * 将签名字符串转换成需要的32位签名
         */
        private fun hexDigest(paramArrayOfByte : ByteArray):String {
            val hexDigits = charArrayOf(48.toChar(),49.toChar(),50.toChar(),51.toChar(),
                52.toChar(),53.toChar(),54.toChar(),55.toChar(),56.toChar(),57.toChar(),
                97.toChar(), 98.toChar(),99.toChar(),100.toChar(),101.toChar(),102.toChar())
            try{
                val localMessageDigest = MessageDigest.getInstance("MD5")
                localMessageDigest.update(paramArrayOfByte)
                val arrayOfByte = localMessageDigest.digest()
                val arrayOfChar = CharArray(32)
                var i = 0
                var j = 0
                while (true) {
                    if( i >= 16){
                        return String(arrayOfChar)
                    }
                    val k = arrayOfByte[i].toInt()
                    //ushr 右移4bit
                    arrayOfChar[j] = hexDigits[0XF and k.ushr(4)]
                    arrayOfChar[++j] = hexDigits[k and 0XF]
                    i++
                    j++
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
            return ""
        }

        @SuppressLint("ServiceCast")
                /**
         * 获取设备的可用内存大小
         */

        fun getDeviceUsableMemory(context: Context) : Int {
            val am = context.getSystemService(Context.ACCOUNT_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            return (mi.availMem/(1024*1024)).toInt()
        }

        /**
         * 获取设备的型号
         */

        fun getMobileModel(): String{
            //model可以为null
            var model: String? = Build.MODEL
            // ?. model不为null 执行trim  为null则为  "" \
            model = model?.trim{it <= ' '} ?: ""
            return model
        }

        /**
         * 获取手机系统的SDK版本
         */

         val sdkVersion : Int
            get() = Build.VERSION.SDK_INT
    }
}