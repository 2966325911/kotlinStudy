package com.kangengine.kotlinstudydemo.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kangengine.kotlinstudydemo.R
import java.lang.Float.compare
import java.lang.IllegalArgumentException

/**
 *
 *   @author : Vic
 *   time    : 2019-01-01 15:49
 *   desc    : 可这折叠展开的TextView
 *
 */
class ExpandableTextView : LinearLayout, View.OnClickListener {
    private var mTextView : TextView ?= null
    private var mButton : ImageView?= null
    private var mRelayout: Boolean  =  false
    /**
     * 用来标记是否折叠的状态
     */
    private var mCollapsed = true
    /**
     * 折叠时的显示图标
     */
    private var mExpandDrawable : Drawable ?= null
    /**
     * 展开时的显示图片
     */
    private var mCollapseDrawable : Drawable ?= null
    /**
     * 设置最多的折叠行数
     */
    private var mMaxCollapsedLines : Int = 0
    /**
     * TextView的最大高度
     */
    private var mTextHeightWidthMaxLines : Int = 0
    private var mMarginBetweenTxtAndBottom : Int = 0
    private var mCollapsedHeight : Int = 0;
    private var mAnimating : Boolean  = false
    private var mAnimAlphaStart : Float = 0.toFloat()
    private var mAnimationDuration : Int = 0

    private val mListener : OnExpandStateChangeListener ?= null

    var text : CharSequence?
    get() = if(mTextView == null) "" else mTextView!!.text
    set(text) {
        mRelayout = true
        mTextView!!.text = text
        visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }

    constructor(context : Context) : super(context)

    constructor(context: Context,attrs:AttributeSet) : super(context,attrs) {
        initView(attrs)
    }

    constructor(context:Context ,attrs: AttributeSet,defStyleAttr : Int) : super(context,attrs,defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs : AttributeSet) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)

        mMaxCollapsedLines = typeArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES)
        mAnimationDuration = typeArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION)
        mAnimAlphaStart = typeArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START)
        mExpandDrawable = typeArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable)
        mCollapseDrawable = typeArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable)

        if(mExpandDrawable == null) {
            mExpandDrawable = getDrawable(context,R.mipmap.ic_action_down_white)
        }

        if(mCollapseDrawable == null) {
            mCollapseDrawable = getDrawable(context,R.mipmap.ic_action_up_white)
        }

        typeArray.recycle()
        orientation = LinearLayout.VERTICAL
        visibility = View.GONE
    }


    override fun setOrientation(orientation: Int) {
        if(LinearLayout.HORIZONTAL == orientation) {
            throw IllegalArgumentException("ExpandrawbleTextView only support Vertical Orientation")
        }
        super.setOrientation(orientation)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(!mRelayout || visibility == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        mRelayout = false

        mButton!!.visibility = View.GONE
        mTextView!!.maxLines = Integer.MAX_VALUE
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if(mTextView!!.lineCount <= mMaxCollapsedLines) {
            return
        }

        mTextHeightWidthMaxLines = getRealTextViewHeight(mTextView!!)

        if(mCollapsed) {
            mTextView!!.maxLines = mMaxCollapsedLines
        }
        mButton!!.visibility = View.VISIBLE

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(mCollapsed) {
            mTextView!!.post{mMarginBetweenTxtAndBottom = height - mTextView!!.height}

            mCollapsedHeight = measuredHeight
        }
    }

    override fun onFinishInflate() {
        findView()
        super.onFinishInflate()
    }

    private fun findView(){
        mTextView = findViewById<View>(R.id.expandable_text) as TextView
        mButton = findViewById<View>(R.id.expand_collapse) as ImageView

        mTextView!!.setOnClickListener(this)
        mButton!!.setOnClickListener(this)
        mButton!!.setImageDrawable(if(mCollapsed) mExpandDrawable else mCollapseDrawable)
    }

    override fun onClick(v: View?) {
        if(mButton!!.visibility != View.VISIBLE) {
            return
        }

        mCollapsed = !mCollapsed

        mButton!!.setImageDrawable(if(mCollapsed) mExpandDrawable else mCollapseDrawable)

        mAnimating = true

        val animation : Animation = if(mCollapsed) {
            ExpandCollapseAnimation(this,height,mCollapsedHeight)
        }else {
            ExpandCollapseAnimation(this,height,height + mTextHeightWidthMaxLines - mTextView!!.height)
        }
        animation.fillAfter = true

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                clearAnimation()
                mAnimating = false
                mListener?.onExpandStateChanged(mTextView,!mCollapsed)
            }

            override fun onAnimationStart(animation: Animation?) {
                applyAlphaAnimation(mTextView,mAnimAlphaStart)
            }

        })

        clearAnimation()
        startAnimation(animation)
    }

    internal  inner class ExpandCollapseAnimation(private val mTargetView : View,
                                                  private val mStartHeight : Int,
                                                  private val mEndHeight : Int) : Animation() {
        init {
            duration = mAnimationDuration.toLong()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            val newHeight = ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight).toInt()
            mTextView!!.maxHeight = newHeight - mMarginBetweenTxtAndBottom
            if(compare(mAnimAlphaStart,1.0f) != 0) {
                applyAlphaAnimation(mTextView,mAnimAlphaStart+interpolatedTime*(1.0f-mAnimAlphaStart))
            }

            mTargetView.layoutParams.height = newHeight
            mTargetView.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }



    interface OnExpandStateChangeListener{
        fun onExpandStateChanged(textView : TextView?,isExpanded:Boolean)
    }

    companion object {
        private val MAX_COLLAPSED_LINES = 0
        private val DEFAULT_ANIM_DURATION = 300
        private val DEFAULT_ANIM_ALPHA_START = 0.7f
        /**
         * 获取TextView真正的高度
         */
        private fun getRealTextViewHeight(textView : TextView):Int{
            val textHeight = textView.layout.getLineTop(textView.lineCount)
            val padding = textView.compoundPaddingTop + textView.compoundPaddingBottom
            return textHeight+padding
        }

        private fun getDrawable(context: Context,@DrawableRes resId : Int) :Drawable? {
            val resources = context.resources
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                resources.getDrawable(resId,context.theme)
            } else {
                ContextCompat.getDrawable(context,resId)
            }
        }

    }

    private fun applyAlphaAnimation(view : View?,alpha:Float) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view!!.alpha = alpha
        } else {
            val alphaAnimation = AlphaAnimation(alpha,alpha)
            alphaAnimation.duration = 0
            alphaAnimation.fillAfter = true
            view!!.startAnimation(alphaAnimation)
        }
    }

}