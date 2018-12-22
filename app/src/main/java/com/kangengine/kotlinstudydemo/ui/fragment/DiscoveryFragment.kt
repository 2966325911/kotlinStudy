package com.kangengine.kotlinstudydemo.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DiscoveryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DiscoveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DiscoveryFragment : BaseFragment() {
    private var mTitle : String? =null
    override fun getLayoutId(): Int = R.layout.fragment_discovery

    override fun initView() {
    }

    override fun lazyLoad() {
    }


    companion object {
        fun  getInstance(title : String) : DiscoveryFragment{
            val fragment = DiscoveryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
}
