package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.CategoryDetailContract
import com.kangengine.kotlinstudydemo.mvp.model.CategoryDetailModel

/**
 *
 *   @author : Vic
 *   time    : 2018-12-26 21:47
 *   desc    :
 *
 */
class CategoryDetailPresenter : BasePresenter<CategoryDetailContract.View>(),
CategoryDetailContract.Presenter{

    private val categoryDetailModel by lazy {
        CategoryDetailModel()
    }

    private var nextPageUrl : String?= null

    /**
     * 获取分类想起的列表信息
     */
    override fun getCategoryDetailList(id: Long) {
        checkViewAttached()
        val disposable = categoryDetailModel.getCategoryDetailList(id)
            .subscribe({
                issue->
                mRootView?.apply {
                    nextPageUrl = issue.nextPageUrl
                    setCateDetailList(issue.itemList)
                }
            },{
                throwable->
                mRootView?.apply {
                    showError(throwable.toString())
                }
            })

        addSubscription(disposable)
    }

    /**
     * 加载更多数据
     */
    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            categoryDetailModel.loadMoreData(it)
                .subscribe({
                    issue->
                    mRootView?.apply {
                        nextPageUrl = issue.nextPageUrl
                        setCateDetailList(issue.itemList)
                    }
                },{throwable->
                    mRootView?.apply {
                        showError(throwable.toString())
                    }
                })
        }

        disposable?.let { addSubscription(it) }

    }
}