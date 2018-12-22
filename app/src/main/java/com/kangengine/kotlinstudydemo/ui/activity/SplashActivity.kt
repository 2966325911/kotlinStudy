package com.kangengine.kotlinstudydemo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.EasyPermissions

class SplashActivity : BaseActivity() {
    private var textTypeface : Typeface? = null
    private var descTypeface : Typeface? = null
    private var alphaAnimation : AlphaAnimation? = null

    init{
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/Lobster-1.4.otf")
        descTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-L-GB-Regular.TTF")

    }
    override fun layoutId(): Int = R.layout.activity_splash

    override fun initData() {
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        tv_app_name.typeface = textTypeface
        tv_splash_desc.typeface = descTypeface
        tv_version_name.text = "v${AppUtils.getVerName(MyApplication.context)}"

        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f,1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                redirectTo()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        checkPermission()
    }

    override fun start() {
    }

    fun redirectTo(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkPermission(){
        val perms = arrayOf(android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this,"KotlinStudy需要以下应用权限，请允许",0
        ,*perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == 0) {
            if(perms.contains(android.Manifest.permission.READ_PHONE_STATE)&&
                    perms.contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if(alphaAnimation !=null){
                    iv_web_icon.startAnimation(alphaAnimation)
                }
            }
        }
    }
}
