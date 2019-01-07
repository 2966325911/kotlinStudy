package com.kangengine.kotlinstudydemo.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.ui.adapter.WatchHistoryAdapter
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import com.kangengine.kotlinstudydemo.utils.WatchHistoryUtils
import kotlinx.android.synthetic.main.activity_watch_history.*
import java.util.*

class WatchHistoryActivity : BaseActivity() {
    private var itemListData = ArrayList<HomeBean.Issue.Item>()
    companion object {
        private const val HISTORY_MAX = 20
    }

    override fun layoutId(): Int {
        return R.layout.activity_watch_history
    }

    override fun initData() {
        multiple_statusView.showLoading()
        itemListData = queryWatchHistory()
    }

    override fun initView() {
        m_toolbar.setNavigationOnClickListener{finish()}
        val mAdapter = WatchHistoryAdapter(this,itemListData,R.layout.item_video_small_card)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        if(itemListData.size > 0) {
            multiple_statusView.showContent()
        } else {
            multiple_statusView.showEmpty()
        }

        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,m_toolbar)
        StatusBarUtil.setPaddingSmart(this,mRecyclerView)
    }

    override fun start() {
    }

    private fun queryWatchHistory():ArrayList<HomeBean.Issue.Item> {
        val watchList = ArrayList<HomeBean.Issue.Item>()
        val hisAll = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context)
        val keys = hisAll.keys.toTypedArray()
        Arrays.sort(keys)
        val keyLength = keys.size

        val hisLength = if(keyLength > HISTORY_MAX) HISTORY_MAX else keyLength
        (1..hisLength).mapTo(watchList){
            WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context,keys[keyLength-it] as String)
            as HomeBean.Issue.Item
        }
        return watchList
    }


}
