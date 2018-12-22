package com.kangengine.kotlinstudydemo.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.hazz.kotlinmvp.mvp.model.bean.TabEntity
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.fragment.DiscoveryFragment
import com.kangengine.kotlinstudydemo.ui.fragment.HomeFragment
import com.kangengine.kotlinstudydemo.ui.fragment.HotFragment
import com.kangengine.kotlinstudydemo.ui.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity() {
    private val mTitles = arrayOf("每日精选","发现","热门","我的")
    //未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.ic_home_normal,
        R.mipmap.ic_discovery_normal,R.mipmap.ic_hot_normal,R.mipmap.ic_mine_normal)
    //被选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.ic_home_selected,
        R.mipmap.ic_discovery_selected,R.mipmap.ic_hot_selected,R.mipmap.ic_mine_selected)

    private val mTabEntities  = ArrayList<CustomTabEntity>()

    private var mHomeFragment : HomeFragment? = null
    private var mDiscoveryFragment : DiscoveryFragment? =null
    private var mHotFragment : HotFragment? = null
    private var mMineFragment : MineFragment? = null

    //默认为 0
    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }

        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)
    }

    override fun layoutId(): Int {
       return R.layout.activity_main
    }

    /**
     * 初始化底部菜单
     */
    private fun initTab(){
        (0 until mTitles.size).mapTo(mTabEntities){
            TabEntity(mTitles[it],mIconSelectIds[it],mIconUnSelectIds[it])
        }

        //为tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
            override fun onTabSelect(position: Int) {
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {
            }
        })
    }


    /**
     * 切换fragment
     */
    private fun switchFragment(position : Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)

        when(position) {
            //每日精选
            0->mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let{
                mHomeFragment = it
                transaction.add(R.id.fl_container,it,"home")
            }
            //发现
            1->mDiscoveryFragment?.let {
                transaction.show(it)
            }?:DiscoveryFragment.getInstance(mTitles[position]).let {
                mDiscoveryFragment = it
                transaction.add(R.id.fl_container,it,"discovery")
            }

            // 热门

            2->mHotFragment?.let {
                transaction.show(it)
            }?:HotFragment.getInstance(mTitles[position]).let {
                mHotFragment = it
                transaction.add(R.id.fl_container,it,"hot")
            }

            // 我的

            3->mMineFragment?.let {
                transaction.show(it)
            }?:MineFragment.getInstance(mTitles[position]).let {
                mMineFragment = it
                transaction.add(R.id.fl_container,it,"mine")
            }
            else ->{

            }
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }

    private fun hideFragments(transaction : FragmentTransaction){
        mHomeFragment ?.let { transaction.hide(it) }
        mHotFragment ?.let { transaction.hide(it) }
        mDiscoveryFragment ?.let { transaction.hide(it)}
        mMineFragment ?.let { transaction.hide(it) }

    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        if(tab_layout != null) {
            outState.putInt("currTabIndex",mIndex)
        }
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun start() {
    }

    private var mExitTime : Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast("在按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
