package com.kangengine.kotlinstudydemo.view.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 21:15
 *   desc    :
 *
 */
class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    //用于缓存已找的界面
    private var mView : SparseArray<View> ? =null
    init {
        mView = SparseArray()
    }

    fun<T:View> getView(viewId : Int) : T{
        //对缓存的View做缓存
        var view : View? = mView?.get(viewId)
        if(view == null) {
            view = itemView.findViewById(viewId)
            mView ?.put(viewId,view)
        }
        return view as T
    }

    fun <T : ViewGroup> getViewGroup(viewId: Int):T{
        //对已有的View做缓存
        var view :View?= mView?.get(viewId)
        if(view == null){
            view = itemView.findViewById(viewId)
            mView?.put(viewId,view)
        }
        return view as T
    }


    //通用的功能进行封装 设置文本 设置条目点击事件 设置图片

    fun setText(viewId:Int,text :CharSequence) : ViewHolder{
        val view = getView<TextView>(viewId)
        view.text = "" + text
        return this
    }

    fun setHintText(viewId: Int,text: CharSequence):ViewHolder {
        val view = getView<TextView>(viewId)
        view.hint = "" + text
        return this
    }

    /**
     * 设置本地图片
     */

    fun setImageResource(viewId: Int,resId:Int) : ViewHolder {
        val iv = getView<ImageView>(viewId)
        iv.setImageResource(resId)
        return this
    }

    /**
     * 设置加载图片资源路径
     */
    fun setImagePath(viewId: Int,imageLoader : HolderImageLoader) : ViewHolder{
        val iv = getView<ImageView>(viewId)
        imageLoader.loadImage(iv,imageLoader.path)
        return this
    }

    abstract class HolderImageLoader(val path : String) {
        abstract fun loadImage(iv:ImageView,path: String)
    }

    /**
     * 设置view的可见性
     */
    fun setViewVisibility(viewId: Int,visibility:Int) : ViewHolder{
        getView<View>(viewId).visibility = visibility
        return this
    }

    fun setOnItemClickListener(listener : View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }


    fun setOnItemLongClickListener(listener: View.OnLongClickListener) {
        itemView.setOnLongClickListener(listener)
    }

}