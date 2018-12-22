package com.kangengine.kotlinstudydemo.ui.fragment


import android.os.Bundle
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment

class MineFragment : BaseFragment() {
    private var mTitle : String? = null
    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initView() {
    }

    override fun lazyLoad() {
    }

    companion object {
        fun getInstance(title : String):MineFragment{
            val fragment = MineFragment()
            fragment.arguments = Bundle()
            fragment.mTitle = title
            return fragment
        }
    }
}
