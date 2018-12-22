package com.kangengine.kotlinstudydemo.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

/**
 *
 *   @author : Vic
 *   time    : 2018-11-24 16:56
 *   desc    :
 *
 */
@GlideModule
class CustomAppGlideModule : AppGlideModule() {

    /**
     * 通过GLideBuilder设置默认的结构
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {

        //重新设置内存限制
        builder.setMemoryCache(LruResourceCache(10*1024*1024))
    }


    /**
     * 不开启清单 避免添加相同的modules两次
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(String::class.java,InputStream::class.java,CustomBaseGlideUrlLoader.Factory())
    }
}