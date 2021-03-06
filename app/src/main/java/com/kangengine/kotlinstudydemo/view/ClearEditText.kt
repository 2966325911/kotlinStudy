package com.kangengine.kotlinstudydemo.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.kangengine.kotlinstudydemo.R
import java.util.jar.Attributes

/**
 *
 *   @author : Vic
 *   time    : 2018-12-23 17:08
 *   desc    :
 *
 */
class ClearEditText @JvmOverloads constructor(context: Context,attrs:AttributeSet?= null,
                                              defStyle:Int = android.R.attr.editTextStyle)
    :EditText(context,attrs,defStyle),View.OnFocusChangeListener,TextWatcher {
    /**
     * EditText右侧的删除按钮
     */
    private var mClearDrawable:Drawable?=null
    private var hasFocus:Boolean = false

    init {
        init()
    }

    @Suppress("DEPRECATION")
    private fun init(){
        //获取EditText的DrawableRight，假如没有设置我们就使用默认的图片，获取图片的顺序是左上右下
        mClearDrawable = compoundDrawables[2]
        if(mClearDrawable == null) {
            mClearDrawable = resources.getDrawable(R.mipmap.ic_action_clear)
        }

        mClearDrawable!!.setBounds(0,0,mClearDrawable!!.intrinsicWidth,mClearDrawable!!.intrinsicHeight)
        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_UP) {
            if(compoundDrawables[2] != null) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val rect = compoundDrawables[2].bounds
                val height = rect.height()
                val distance = (getHeight() - height)/2
                val isInnerWidth = x > width - totalPaddingRight && x < width - paddingRight
                val isInnerHeight = y > distance && y < distance + height
                if(isInnerWidth && isInnerHeight) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if(hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
    }

    private fun setClearIconVisible(visible : Boolean) {
        val right = if(visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0],
            compoundDrawables[1],right,compoundDrawables[3])
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if(hasFocus) {
            setClearIconVisible(text.isNotBlank())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

}