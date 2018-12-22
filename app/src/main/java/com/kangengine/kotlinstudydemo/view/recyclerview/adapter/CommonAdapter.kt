package com.kangengine.kotlinstudydemo.view.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kangengine.kotlinstudydemo.view.recyclerview.MultipleType
import com.kangengine.kotlinstudydemo.view.recyclerview.ViewHolder

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 21:38
 *   desc    :
 *
 */
abstract class CommonAdapter<T>(var mContext:Context,var mData:ArrayList<T>,
                       private var mLayoutId : Int):RecyclerView.Adapter<ViewHolder>() {
    private val TAG = CommonAdapter::class.java.simpleName
    protected var mInflater : LayoutInflater? = null
    private var mTypeSupport : MultipleType<T>?= null

    private var mItemClickListener : OnItemClickListener? = null
    private var mItemLongClickListener : OnItemLongClickListener? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    constructor(context: Context,data:ArrayList<T>,typeSupport : MultipleType<T>):this(context,data,-1){
        this.mTypeSupport = typeSupport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(mTypeSupport != null) {
            mLayoutId = viewType
        }

        val view = mInflater?.inflate(mLayoutId,parent,false)
        return ViewHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        return mTypeSupport?.getLayoutId(mData[position],position)?:super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG,"commAdapter==omBindViewHolder===start")
        bindData(holder,mData[position],position)
        Log.d(TAG,"commAdapter==omBindViewHolder===end")
        mItemClickListener?.let {
            holder.itemView.setOnClickListener{
                mItemClickListener!!.onItemClick(mData[position],position)
            }
        }
        mItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener{
                mItemLongClickListener!!.onItemLongClick(mData[position],position)
            }
        }
    }

    protected abstract fun bindData(holder: ViewHolder,data : T,position: Int)

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener){
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }

}