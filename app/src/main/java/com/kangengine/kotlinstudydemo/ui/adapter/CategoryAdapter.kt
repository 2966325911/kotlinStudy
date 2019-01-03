package com.kangengine.kotlinstudydemo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.MyApplication
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.glide.GlideApp
import com.kangengine.kotlinstudydemo.ui.activity.CategoryDetailActivity
import com.kangengine.kotlinstudydemo.view.recyclerview.ViewHolder
import com.kangengine.kotlinstudydemo.view.recyclerview.adapter.CommonAdapter
import kotlinx.android.synthetic.main.item_catergory.view.*
import org.w3c.dom.Text

/**
 *
 *   @author : Vic
 *   time    : 2018-12-25 22:14
 *   desc    :
 *
 */
class CategoryAdapter(mContext: Context,categoryList:ArrayList<CategoryBean>,layoutId:Int ) :
CommonAdapter<CategoryBean>(mContext,categoryList,layoutId){
    private var textTypeface : Typeface ?= null

    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
    }

    /**
     * 设置新数据
     */
    fun setData(categoryList:ArrayList<CategoryBean>) {
        mData.clear()
        mData = categoryList
        notifyDataSetChanged()
    }


    /**
     * 绑定数据
     */
    override fun bindData(holder: ViewHolder, data: CategoryBean, position: Int) {
        holder.setText(R.id.tv_category_name,"#${data.name}")
        //设置字体
        holder.getView<TextView>(R.id.tv_category_name).typeface = textTypeface

        holder.setImagePath(R.id.iv_category,object:ViewHolder.HolderImageLoader(data.bgPicture) {
            override fun loadImage(iv: ImageView, path: String) {
                GlideApp.with(mContext)
                    .load(path)
                    .placeholder(R.color.color_darker_gray)
                    .transition(DrawableTransitionOptions().crossFade())
                    .thumbnail(0.5f)
                    .into(iv)
            }
        })


        holder.setOnItemClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(mContext as Activity, CategoryDetailActivity::class.java)
                intent.putExtra(Constants.BUNDLE_CATEGORY_DATA,data)
                mContext.startActivity(intent)
            }
        })
    }
}