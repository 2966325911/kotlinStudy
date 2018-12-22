package com.kangengine.kotlinstudydemo.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.widget.MultipleStatusView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *
 *   @author : Vic
 *   time    : 2018-11-20 21:19
 *   desc    :
 *
 */
abstract class BaseActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {
    /**
     * 多种状态的View的切换
     */
    protected var mLayoutStatusView : MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId())

        initData()
        initView()
        start()
        initListener()
    }

    private fun initListener(){
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     * 加载布局
     */
    abstract fun layoutId() : Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText : EditText,mContext : Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText,InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText,mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken,0)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getRefWatcher(this)?.watch(this)
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
            Toast.makeText(this,"已拒绝权限" + sb+"并不在询问",Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }


}

