package com.kangengine.kotlinstudydemo.mvp.presenter

import com.kangengine.kotlinstudydemo.base.BasePresenter
import com.kangengine.kotlinstudydemo.mvp.contract.CategoryContract
import com.kangengine.kotlinstudydemo.mvp.model.CategoryModel
import com.kangengine.kotlinstudydemo.net.exception.ExceptionHandle

/**
 *
 *   @author : Vic
 *   time    : 2018-12-25 22:06
 *   desc    :
 *
 */
class CategoryPresenter : BasePresenter<CategoryContract.View>(),CategoryContract.Presenter {
    private val categoryModel: CategoryModel by lazy {
        CategoryModel()
    }

    /**
     * 分类
     */
    override fun getCategoryData() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = categoryModel.getCategoryData()
            .subscribe({categoryList->
                mRootView?.apply {
                    dismissLoading()
                    showCategory(categoryList)
                }
            },{t->
                mRootView?.apply {
                    //处理异常
                    showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

}