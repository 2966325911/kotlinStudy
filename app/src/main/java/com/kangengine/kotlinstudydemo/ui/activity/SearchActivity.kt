package com.kangengine.kotlinstudydemo.ui.activity

import android.animation.Animator
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.flexbox.*
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.base.BaseActivity
import com.kangengine.kotlinstudydemo.mvp.contract.SearchContract
import com.kangengine.kotlinstudydemo.mvp.presenter.SearchPresenter
import com.kangengine.kotlinstudydemo.net.exception.ErrorStatus
import com.kangengine.kotlinstudydemo.showToast
import com.kangengine.kotlinstudydemo.ui.adapter.CategoryDetailAdapter
import com.kangengine.kotlinstudydemo.ui.adapter.HotKeyWordsAdapter
import com.kangengine.kotlinstudydemo.utils.CleanLeakUtils
import com.kangengine.kotlinstudydemo.utils.StatusBarUtil
import com.kangengine.kotlinstudydemo.view.ViewAnimUtils
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(),SearchContract.View {
    private var mHotKeyWordsAdapter : HotKeyWordsAdapter? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private var mTextTypeface : Typeface?= null
    private var keyWords : String?= null
    /**
     * 是否加载更多
     */
    private var loadingMore = false
    private val mPresenter by lazy {
        SearchPresenter()
    }

    private val mResultAdapter by lazy {
        CategoryDetailAdapter(this,itemList,R.layout.item_category_detail)
    }

    init {
        mPresenter.attachView(this)
        //细黑简体字体
        mTextTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }


    override fun layoutId(): Int {
        return R.layout.activity_search
    }

    override fun initData() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpEnterAnimation()
            setUpExitAnimation()
        } else {
            setUpView()
        }
    }

    override fun initView() {
        tv_title_tip.typeface = mTextTypeface
        tv_hot_search_words.typeface = mTextTypeface
        //初始化查询结果的RecycleView
        mRecyclerView_result.layoutManager = LinearLayoutManager(this)
        mRecyclerView_result.adapter = mResultAdapter

        //实现自动加载
        mRecyclerView_result.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView_result.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView_result.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        //取消
        tv_cancel.setOnClickListener{onBackPressed()}
        //键盘的搜索按钮
        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeSoftKeyboard()
                    keyWords = et_search_view.text.toString().trim()
                    if(keyWords.isNullOrEmpty()) {
                        showToast("请输入你感兴趣的关键词")
                    } else {
                        mPresenter.querySearchData(keyWords!!)
                    }
                }
                return false
            }
        })

        mLayoutStatusView= multipleStatusView

        //设置状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)

    }

    override fun closeSoftKeyboard() {
        closeKeyBord(et_search_view,applicationContext)
    }

    override fun start() {
        //请求热门关键词
        mPresenter.requestHotWordData()
    }
    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }

    /**
     * 设置热门关键词
     */
    override fun setHotWordData(string: ArrayList<String>) {
        showHotWordView()
        mHotKeyWordsAdapter = HotKeyWordsAdapter(this,string,R.layout.item_flow_text)
        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        //按正常方向换行
        flexBoxLayoutManager.flexWrap =  FlexWrap.WRAP
        //主轴为水平方向，起点在左端
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW
        //定义项目在副轴上如何对齐
        flexBoxLayoutManager.alignItems = AlignItems.CENTER
        //多个轴对齐方式
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START

        mRecycleView_hot.layoutManager = flexBoxLayoutManager
        mRecycleView_hot.adapter = mHotKeyWordsAdapter

        //设置Tag的点击事件
        mHotKeyWordsAdapter?.setOnTagItemClickListener {
            closeSoftKeyboard()
            keyWords = it
            mPresenter.querySearchData(it)
        }

    }

    /**
     * 设置搜索结果
     */
    override fun setSearchResult(issue: HomeBean.Issue) {
        loadingMore = false
        hideHotWordView()
        tv_search_count.visibility = View.VISIBLE
        tv_search_count.text = String.format(resources.getString(R.string.search_result_count),
            keyWords,issue.total)
        itemList = issue.itemList
        mResultAdapter.addData(issue.itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if(errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    /**
     * 没有找到相匹配的内容
     */
    override fun setEmptyView() {
        showToast("抱歉，没有找到相匹配的内容")
        hideHotWordView()
        tv_search_count.visibility = View.GONE
        mLayoutStatusView?.showEmpty()
    }

    private fun showHotWordView(){
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE
    }

    private fun hideHotWordView(){
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }



    /**
     * 退场动画
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation(){
        val fade = Fade()
        window.returnTransition = fade
        fade.duration = 300

    }

    /**
     * 进场动画
     */

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation(){
        val transition = TransitionInflater.from(this)
            .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionStart(transition: Transition) {
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }
        })
    }



    private fun setUpView(){
        val animation = AnimationUtils.loadAnimation(this,android.R.anim.fade_in)
        animation.duration = 300
        rel_container.startAnimation(animation)
        rel_container.visibility = View.VISIBLE
        //打开软键盘
        openKeyBord(et_search_view,applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow(){
        ViewAnimUtils.animateRevealShow(this,rel_frame,
            fab_circle.width/2,R.color.backgroundColor,
            object :ViewAnimUtils.OnRevealAnimationListener{
                override fun onRevealHide() {
                }

                override fun onRevealShow() {
                    setUpView()
                }
            })
    }


    /**
     * 返回事件
     */
    override fun onBackPressed() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimUtils.animateRevealHide(this,rel_frame,
                fab_circle.width/2,R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener{
                    override fun onRevealHide() {
                        defaultBackPressed()
                    }

                    override fun onRevealShow() {
                    }
                })
        }else {
            defaultBackPressed()
        }
    }

    /**
     * 默认返回
     */
    private fun defaultBackPressed(){
        closeSoftKeyboard()
        super.onBackPressed()
    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        mPresenter.detachView()
        mTextTypeface = null
    }
}
