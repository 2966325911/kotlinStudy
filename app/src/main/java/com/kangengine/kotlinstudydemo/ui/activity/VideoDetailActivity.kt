package com.kangengine.kotlinstudydemo.ui.activity

import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.glide.GlideApp
import com.kangengine.kotlinstudydemo.mvp.contract.VideoDetailContract
import com.kangengine.kotlinstudydemo.mvp.presenter.VideoDetailPresenter
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.VideoDetailAdapter
import com.kangengine.kotlinstudydemo.utils.CleanLeakUtils
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import com.kangengine.kotlinstudydemo.utils.WatchHistoryUtils
import com.kangengine.kotlinstudydemo.view.VideoListener
import com.scwang.smartrefresh.header.MaterialHeader
import com.shuyu.gsyvideoplayer.listener.LockClickListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VideoDetailActivity : BaseActivity(),VideoDetailContract.View {

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }

    /**
     * 第一次调用的时候初始化
     */
    private val mPresenter by lazy {
        VideoDetailPresenter()
    }

    private val mAdateper by lazy{
        VideoDetailAdapter(this,itemList)
    }

    private val mFormat by lazy{
        SimpleDateFormat("yyyyMMDDHHmmss");
    }

    /**
     * item详细数据
     */
    private lateinit var itemData : HomeBean.Issue.Item
    private var orientationUtils : OrientationUtils? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private var isPlay : Boolean = false
    private var isPause : Boolean = false
    private var isTransition : Boolean  = false
    private var transition : Transition? = null
    private var mMaterialHeader : MaterialHeader?= null
    override fun layoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initView() {
        mPresenter.attachView(this)
        //过渡动画
        initTransition()
        initVideoViewConfig()

        mRecycleView.layoutManager = LinearLayoutManager(this)
        mRecycleView.adapter = mAdateper

        mAdateper.setOnItemDetailClick { mPresenter.loadVideoInfo(it) }

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this,mVideoView)

        /***  下拉刷新 ***/
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            loadVideoInfo()
        }

        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black,R.color.color_title_bg)
    }

    private fun initVideoViewConfig(){
        //设置旋转
        orientationUtils = OrientationUtils(this,mVideoView)
        //是否旋转
        mVideoView.isRotateViewAuto = false
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true)

        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideApp.with(this)
            .load(itemData.data?.cover?.feed)
            .centerCrop()
            .into(imageView)

        mVideoView.thumbImageView = imageView

        mVideoView.setStandardVideoAllCallBack(object: VideoListener {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                orientationUtils?.isEnable = true
                isPlay = true
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                super.onAutoComplete(url, *objects)

            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
                super.onPlayError(url, *objects)
                showToast("播放失败")
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                super.onEnterFullscreen(url, *objects)

            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
            }
        })
        //设置返回按键功能
        mVideoView.backButton.setOnClickListener({onBackPressed()})
        //设置全屏按钮功能
        mVideoView.fullscreenButton.setOnClickListener{
            //直接横屏
            orientationUtils?.resolveByClick()

            mVideoView.startWindowFullscreen(this,true,true)
        }

        //锁屏事件
        mVideoView.setLockClickListener(object:LockClickListener{
            override fun onClick(view: View?, lock: Boolean) {
                //配合下方的onConfigurationChanged
                orientationUtils?.isEnable = !lock
            }
        })
    }


    override fun start() {
    }

    override fun initData() {
        itemData = intent.getSerializableExtra(Constants.BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
        isTransition = intent.getBooleanExtra(TRANSITION,false)

        saveWatchVideoHistoryInfo(itemData)
    }

    private fun saveWatchVideoHistoryInfo(watchtItem:HomeBean.Issue.Item){
        val historyMap = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME,
            MyApplication.context) as Map<*,*>

        for((key,_) in historyMap) {
            if(watchtItem == WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME,
                    MyApplication.context,key as String))

                WatchHistoryUtils.remove(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context,key)
        }

        WatchHistoryUtils.putObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context,
            watchtItem,"" + mFormat.format(Date()))
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }

    override fun setVideo(url: String) {
        mVideoView.setUp(url,false,"")
        //开始自动播放
        mVideoView.startPlayLogic()
    }

    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
        itemData = itemInfo
        mAdateper.addData(itemInfo)
        //请求相关的最新等视频
        mPresenter.requestRelatedVideo(itemInfo.data?.id?:0)
    }

    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        mAdateper.addData(itemList)
        this.itemList = itemList
    }

    override fun setBackground(url: String) {
        GlideApp.with(this)
            .load(url)
            .centerCrop()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .transition(DrawableTransitionOptions().crossFade())
            .into(mVideoBackground)
    }

    override fun setErrorMsg(errMsg: String) {
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(isPlay && !isPause) {
            mVideoView.onConfigurationChanged(this,newConfig,orientationUtils)
        }
    }

    /**
     * 加载视频信息
     */
    fun loadVideoInfo(){
        mPresenter.loadVideoInfo(itemData)
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if(StandardGSYVideoPlayer.backFromWindowFull(this))
            return
        mVideoView.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()

        if(isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run {
            super.onBackPressed()
        }else {
            finish()
            overridePendingTransition(R.anim.anim_out,R.anim.anim_in)
        }
    }

    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.releaseListener()
        mPresenter.detachView()
    }

    private fun getCurPlay():GSYVideoPlayer {
        return if(mVideoView.fullWindowPlayer != null) {
            mVideoView.fullWindowPlayer
        }else mVideoView
    }

    private fun initTransition(){
        if(isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        }else {
            loadVideoInfo()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener(){
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {

            }

            override fun onTransitionEnd(transition: Transition?) {
                loadVideoInfo()
                transition?.removeListener(this)
            }
        })
    }




}


