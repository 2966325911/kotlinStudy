package com.kangengine.kotlinstudydemo.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment

import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment
import com.kangengine.kotlinstudydemo.base.BaseFragmentAdapter
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import com.kangengine.kotlinstudydemo.view.TabLayoutHelper
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * 发现
 */
class DiscoveryFragment : BaseFragment() {
    private var mTitle : String? =null
    private val tabList = ArrayList<String>()
    private val fragments = ArrayList<Fragment>()

    override fun getLayoutId(): Int = R.layout.fragment_discovery
    companion object {
        fun  getInstance(title : String) : DiscoveryFragment{
            val fragment = DiscoveryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun initView() {
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it,toolbar) }

        tv_header_title.text = mTitle

        tabList.add("关注")
        tabList.add("分类")
        fragments.add(FollowFragment.getInstance("关注"))
        fragments.add(CategoryFragment.getInstance("分类"))
        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager,fragments,tabList)
        mTabLayout.setupWithViewPager(mViewPager)
        TabLayoutHelper.setUpIndicatorWidth(mTabLayout)
    }

    override fun lazyLoad() {
    }



}
