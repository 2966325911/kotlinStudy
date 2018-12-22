package com.kangengine.kotlinstudydemo.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.widget.MultipleStatusView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *
 *   @author : Vic
 *   time    : 2018-11-20 21:56
 *   desc    :
 *
 */
abstract class BaseFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    /**
     * 多种状态View的切换
     */
    protected var mLayoutStatusView : MultipleStatusView?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),null)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            lazyLoadDataIfPrepared()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()

        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun lazyLoadDataIfPrepared(){
        if(userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }


    /**
     * 加载布局
     */
    abstract fun getLayoutId(): Int
    /**
     * 初始化view
     */
    abstract  fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        activity?.let { MyApplication.getRefWatcher(it)?.watch(activity) }
    }



    /**
     *  重写要申请权限的Activity或者Fragment的onRequestPermissionResult()方法
     *  在里面调用EasyPermission.onRequestPermissionResult(),实现回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    /**
     * 当权限成功申请的时候执行回调
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i("EasePermission","获取成功的权限$perms");
    }

    /**
     * 当权限申请被拒绝的时候
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val sb = StringBuffer()
        for(str in perms){
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2,sb.length,"")

        //用户点击拒绝的时候回调
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }
}