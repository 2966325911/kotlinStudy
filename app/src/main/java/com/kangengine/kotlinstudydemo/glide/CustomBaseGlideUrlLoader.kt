package com.kangengine.kotlinstudydemo.glide

import android.renderscript.ScriptGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream
import java.util.regex.Pattern

/**
 *
 *   @author : Vic
 *   time    : 2018-11-24 17:00
 *   desc    :
 *
 */
class CustomBaseGlideUrlLoader(concreteLoader:ModelLoader<GlideUrl,InputStream>,modelCache:ModelCache<String,GlideUrl>):
BaseGlideUrlLoader<String>(concreteLoader,modelCache){
    override fun handles(model: String): Boolean {
        return true
    }

    override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
        val m = PATTER.matcher(model)
        var bestBucket:Int
        if(m.find()) {
            val found = m.group(1).split("-".toRegex()).dropLastWhile{
                it.isEmpty() }.toTypedArray()

            for(bucketStr in found){
                bestBucket = Integer.parseInt(bucketStr)
                if(bestBucket >= width) {
                    break
                }
            }
        }
        return model

    }

    class Factory : ModelLoaderFactory<String,InputStream>{
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return CustomBaseGlideUrlLoader(multiFactory.build(GlideUrl::class.java,InputStream::class.java), urlCache)
        }

        override fun teardown() {

        }
    }
    companion object {
        private val urlCache = ModelCache<String,GlideUrl>(150)

        /**
         * url的匹配规则
         */
        private val PATTER = Pattern.compile("__w-((?:-?\\d+)+)__")
    }
}