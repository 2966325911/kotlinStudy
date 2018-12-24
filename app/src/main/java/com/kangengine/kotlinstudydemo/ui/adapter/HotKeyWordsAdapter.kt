package com.kangengine.kotlinstudydemo.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayoutManager
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.view.recyclerview.ViewHolder
import com.kangengine.kotlinstudydemo.view.recyclerview.adapter.CommonAdapter
import kotlinx.android.synthetic.main.item_home_content.view.*
import org.w3c.dom.Text

/**
 *
 *   @author : Vic
 *   time    : 2018-12-23 16:42
 *   desc    :
 *
 */
class HotKeyWordsAdapter(mContext : Context,mList:ArrayList<String>,layoutId:Int):
CommonAdapter<String>(mContext,mList,layoutId){

    /**
     * kotlin中的函数可以作为参数，写callback的时候，不用interface了
     */
    private var mOnTagItemClick:((tag:String)->Unit)? = null

    fun setOnTagItemClickListener(onTagItemClickListener:(tag:String)->Unit) {
        this.mOnTagItemClick = onTagItemClickListener
    }

    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        holder.setText(R.id.tv_title,data)
        val params = holder.getView<TextView>(R.id.tv_title).layoutParams
        if(params is FlexboxLayoutManager.LayoutParams) {
            params.flexGrow = 1.0f
        }

        holder.setOnItemClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                mOnTagItemClick?.invoke(data)
            }
        })
    }

}