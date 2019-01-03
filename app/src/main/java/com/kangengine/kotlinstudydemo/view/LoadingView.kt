package com.kangengine.kotlinstudydemo.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.kangengine.kotlinstudydemo.R
import kotlinx.android.synthetic.main.loading_view.view.*
import java.lang.ref.WeakReference


/**
 *
 *   @author : Vic
 *   time    : 2019-01-01 17:03
 *   desc    :
 *
 */
class LoadingView : View {
    private var mOuterCircleRadius : Int = 0
    private var mOuterCircleColor : Int = 0
    private var mInnerTriangleRadius : Int = 0
    private var mInnerTriangleColor : Int = 0
    private var mBackgroundColor : Int = 0
    private var mStrokeWidth : Int = 0
    private var mIsNeedBackground  = false

    private var mPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTrianglePaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBackGroundPaint  : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var isReverse = false
    private var mProgress  = 0
    private var mStartAngle = -90
    private var mRotateAngle = 0
    private var mDel  = 30

    private var mRectF : RectF ?= null
    private var mRoundRectF  : RectF ?= null
    private var mPath : Path? = null
    private var mRotateCenter : PointF ?= null

    private var mHandler : MyHandler?= null

    constructor(context : Context): super(context)

    constructor(context: Context,attrs:AttributeSet):super(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet,defStyleAttr : Int) :super(context,attrs,defStyleAttr) {
        var typedArray : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView)
        mOuterCircleRadius = typedArray.getDimensionPixelSize(R.styleable.LoadingView_outerCircleRadius,50)
        mOuterCircleColor = typedArray.getColor(R.styleable.LoadingView_outerCircleColor, 0xFF228B22.toInt())
        mInnerTriangleRadius = typedArray.getDimensionPixelSize(R.styleable.LoadingView_innerTriangleRadius,25)
        mInnerTriangleColor = typedArray.getColor(R.styleable.LoadingView_innerTriangleColor,0xFF228B22.toInt())
        mIsNeedBackground = typedArray.getBoolean(R.styleable.LoadingView_isNeedBackground,true)
        mBackgroundColor = typedArray.getColor(R.styleable.LoadingView_backgroundColor,0xBB222222.toInt())
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingView_strokeWidth,5)

        typedArray.recycle()
        init()
    }

    private fun init(){
        mPaint.color = mOuterCircleColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat()

        mTrianglePaint.color = mInnerTriangleColor

        mBackGroundPaint.color = mBackgroundColor

        mPath = Path()
        mRotateCenter = PointF()
        mRoundRectF = RectF()
        mHandler = MyHandler(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mOuterCircleRadius = Math.min(mOuterCircleRadius,(Math.min(w-paddingLeft-paddingRight,
            ((h-paddingTop-paddingBottom)-4 * mPaint.strokeWidth).toInt())/2))
        if(mOuterCircleRadius < 0) {
            mStrokeWidth = Math.min(w-paddingRight - paddingLeft,h-paddingTop-paddingBottom)/2
            mOuterCircleRadius = Math.min(w - paddingRight - paddingLeft,h-paddingTop-paddingBottom)/4
        }

        var left = ((w - 2 * mOuterCircleRadius)/2).toFloat()
        var top = ((h - 2 * mOuterCircleRadius)/2).toFloat()
        var diameter = (2 * mOuterCircleRadius).toFloat()
        mRectF = RectF(left.toFloat(), top.toFloat(), (left+diameter).toFloat(), (top+diameter).toFloat())

        mInnerTriangleRadius = if(mInnerTriangleRadius < mOuterCircleRadius) mInnerTriangleRadius else 3 * mOuterCircleRadius / 5
        if(mInnerTriangleRadius < 0) {
            mInnerTriangleRadius = 0
        }

        var centerX = left + mOuterCircleRadius
        var centerY = top + mOuterCircleRadius
        mPath?.moveTo(centerX-mInnerTriangleRadius / 2,(centerY-Math.sqrt(3.0)*mInnerTriangleRadius/2).toFloat())
        mPath?.lineTo(centerX - mInnerTriangleRadius/2,(centerY+Math.sqrt(3.0)*mInnerTriangleRadius/2).toFloat())
        mPath?.close()

        mRotateCenter?.set((measuredWidth/2).toFloat(),(measuredHeight/2).toFloat())
        mRoundRectF?.left = 0.0f
        mRoundRectF?.top = 0.0f
        mRoundRectF?.right = w.toFloat()
        mRoundRectF?.bottom = h.toFloat()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSize(widthMeasureSpec,140),measureSize(heightMeasureSpec,140))
    }

    private fun measureSize(measureSpec : Int,defaultSize : Int): Int {
        var specMode = MeasureSpec.getMode(measureSpec)
        var specSize = MeasureSpec.getSize(measureSpec)
        var resultSize = defaultSize
        when(specMode) {
            MeasureSpec.EXACTLY-> resultSize = defaultSize
            MeasureSpec.AT_MOST,MeasureSpec.UNSPECIFIED->Math.min(specSize,defaultSize)
            else -> resultSize = defaultSize
        }

        return resultSize
    }

    override fun onDraw(canvas: Canvas) {

        if(mIsNeedBackground) {
            canvas.drawRoundRect(mRoundRectF,8f,8f,mBackGroundPaint)
        }

        if(isReverse) {
            mProgress -= mDel
            mStartAngle += mDel
            if(mStartAngle >= 270) {
                mStartAngle = -90
                isReverse = false
            }

            mRotateAngle += mDel
            if(mRotateAngle == 360) {
                mRotateAngle = 0
            }

            canvas.save()
            canvas.rotate(mRotateAngle.toFloat(),mRotateCenter!!.x, mRotateCenter!!.y)
            canvas.drawPath(mPath,mTrianglePaint)
        }else {
            mProgress  += mRotateAngle
            if(mProgress >= 360) {
                isReverse = false
            }
            canvas.drawPath(mPath,mTrianglePaint)

        }
        canvas.drawArc(mRectF,mStartAngle.toFloat(),mProgress.toFloat(),false,mPaint)
        mHandler?.sendEmptyMessageDelayed(MyHandler.REFRESH_VIEW,80)
    }

    class MyHandler : Handler {
       companion object { val REFRESH_VIEW : Int = 0 }

        var mLoadingViewWeakReference : WeakReference<LoadingView>

        constructor(loadingView: LoadingView) {
            mLoadingViewWeakReference = WeakReference(loadingView)
        }

        override fun handleMessage(msg: Message) {
            if(mLoadingViewWeakReference.get() != null){
                when (msg.what) {
                    REFRESH_VIEW -> mLoadingViewWeakReference.get()!!.postInvalidate()
                }
            }
        }
    }

}