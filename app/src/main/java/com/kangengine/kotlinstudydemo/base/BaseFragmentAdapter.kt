package com.kangengine.kotlinstudydemo.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 *   @author : Vic
 *   time    : 2018-11-20 22:06
 *   desc    : 该类内每生成的每一个fragment都将保存在内存之中
 *   试用与静态的页面，数据量较少的那种，如果需要页面多，数据量大，
 *   使用FragmentStatePagerAdapter这种
 *
 */
class BaseFragmentAdapter : FragmentPagerAdapter {

    private var fragmentList : List<Fragment>? = ArrayList()
    private var mTitles : List<String>? = null

    constructor(fm : FragmentManager,fragmentList: List<Fragment>) : super(fm) {
        this.fragmentList = fragmentList
    }

    constructor(fm: FragmentManager,fragmentList: List<Fragment>,mTitles : List<String>):super(fm) {
        this.mTitles = mTitles
        setFragments(fm,fragmentList,mTitles)

    }


    private fun setFragments(fm: FragmentManager,fragments: List<Fragment>,mTitles: List<String>){
        this.mTitles = mTitles
        if(this.fragmentList != null){
            val ft = fm.beginTransaction()
            fragmentList?.forEach {
                ft.remove(it)
            }
            ft?.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
        this.fragmentList = fragments
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if( null != mTitles)mTitles!![position] else ""
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }
}