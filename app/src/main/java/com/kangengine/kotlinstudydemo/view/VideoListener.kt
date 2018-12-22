package com.kangengine.kotlinstudydemo.view

import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack

/**
 *
 *   @author : Vic
 *   time    : 2018-12-16 20:55
 *   desc    :
 *
 */
interface VideoListener : StandardVideoAllCallBack {
    //加载完成，objects[0]是title，objects[1]是当前所处播放器（全屏或非全屏）
    override fun onPrepared(url: String?, vararg objects: Any?) {
    }

    override fun onClickStartIcon(url: String?, vararg objects: Any?) {
    }

    override fun onClickStartError(url: String?, vararg objects: Any?) {
    }

    override fun onClickStop(url: String?, vararg objects: Any?) {
    }


    override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onClickResume(url: String?, vararg objects: Any?) {
    }

    override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onClickSeekbar(url: String?, vararg objects: Any?) {
    }

    override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onAutoComplete(url: String?, vararg objects: Any?) {
    }

    override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
    }

    override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
    }

    override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
    }

    override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
    }


    override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
    }

    override fun onPlayError(url: String?, vararg objects: Any?) {
    }

    override fun onClickStartThumb(url: String?, vararg objects: Any?) {
    }

    override fun onClickBlank(url: String?, vararg objects: Any?) {
    }


    override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
    }

}