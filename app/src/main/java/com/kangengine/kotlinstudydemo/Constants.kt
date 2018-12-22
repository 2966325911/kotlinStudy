package com.kangengine.kotlinstudydemo

/**
 *
 *   @author : Vic
 *   time    : 2018-11-17 16:04
 *   desc    :常量
 *
 */
class Constants private constructor() {
    companion object {
        val BUNDLE_VIDEO_DATA = "video_data"
        val BUNDLE_CATEGORY_DATA = "ctegory_data"

        //sp 存储的文件名
        val FILE_WATCH_HISTORY_NAME = "watch_history_file"

        //收藏视屏花缓存的文件名
        val FILE_COLLECTION_NAME  = "collection_file"
    }
}