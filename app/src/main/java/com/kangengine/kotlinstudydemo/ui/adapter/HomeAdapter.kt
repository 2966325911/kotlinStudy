package com.kangengine.kotlinstudydemo.ui.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.util.Log
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.durationFormat
import com.kangengine.kotlinstudydemo.glide.GlideApp
import com.kangengine.kotlinstudydemo.ui.activity.VideoDetailActivity
import com.kangengine.kotlinstudydemo.view.recyclerview.ViewHolder
import com.kangengine.kotlinstudydemo.view.recyclerview.adapter.CommonAdapter
import io.reactivex.Observable
import java.sql.BatchUpdateException

/**
 *
 *   @author : Vic
 *   time    : 2018-12-02 21:08
 *   desc    :
 *
 */
class HomeAdapter(context: Context,data:ArrayList<HomeBean.Issue.Item>):
    CommonAdapter<HomeBean.Issue.Item>(context,data,-1) {

    private val TAG : String = HomeAdapter::class.java.simpleName
    //banner作为RecycleView的第一项
    var bannerItemSize = 0

    companion object {
        private const val ITEM_TYPE_BANNER = 1 //banner 类型
        private const val ITEM_TYPE_TEXT_HEADER = 2 //textHeader
        private const val ITEM_TYPE_CONTENT = 3
    }

    /**
     * 设置Banner大小
     */
    fun setBannerSize(count : Int) {
        bannerItemSize = count
    }

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }

    /**
     * 得到Item的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 ->
                ITEM_TYPE_BANNER
            mData[position + bannerItemSize - 1].type == "textHeader" ->
                ITEM_TYPE_TEXT_HEADER
            else ->
                ITEM_TYPE_CONTENT
        }
    }

    /**
     * 得到RecycleView Item数量 Banner作为一个item
     */
    override fun getItemCount(): Int {
        return when {
            mData.size > bannerItemSize ->mData.size - bannerItemSize + 1
            mData.isEmpty() -> 0
            else -> 1
        }
    }

    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        Log.d(TAG,"data===" + data)
        when(getItemViewType(position)) {
            ITEM_TYPE_BANNER ->{
                Log.d(TAG,"item_type_banner==")
                val bannerItemData : ArrayList<HomeBean.Issue.Item> = mData.take(bannerItemSize)
                    .toCollection(ArrayList())
                val bannerFeedList = ArrayList<String>()
                val bannerTitleList = ArrayList<String>()
                Log.d(TAG,"bannerItemData=="+bannerItemData)
                //去除banner显示的img 和title
                Observable.fromIterable(bannerItemData)
                    .subscribe { list ->
                        bannerFeedList.add(list.data ?.cover?.feed?:"")
                        bannerTitleList.add(list.data?.title?:"")
                    }
                with(holder){
                    getView<BGABanner>(R.id.banner).run {
                        setAutoPlayAble(bannerFeedList.size > 1)
                        setData(bannerFeedList,bannerTitleList)
                        setAdapter{ banner,_,feedImageUrl,position->
                            GlideApp.with(context)
                                .load(feedImageUrl)
                                .transition(DrawableTransitionOptions().crossFade())
                                .placeholder(R.drawable.placeholder_banner)
                                .into(banner.getItemImageView(position))
                        }
                    }
                }
                //使用没有用到的参数 在kotlin中用“ _”代替
                holder.getView<BGABanner>(R.id.banner).setDelegate { _, imageView, _,  i->
                    goToVideoPlay(mContext as Activity,imageView,bannerItemData[i])
                }

            }

            //TextHeader
            ITEM_TYPE_TEXT_HEADER ->{
                holder.setText(R.id.tvHeader,mData[position + bannerItemSize-1].data?.text?:"")
            }

            // context
            ITEM_TYPE_CONTENT ->{
                setVideoItem(holder,mData[position + bannerItemSize - 1])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ITEM_TYPE_BANNER->
                ViewHolder(inflaterView(R.layout.item_home_bannder,parent))
            ITEM_TYPE_TEXT_HEADER ->
                ViewHolder(inflaterView(R.layout.item_home_header,parent))
            else ->{
                ViewHolder(inflaterView(R.layout.item_home_content,parent))
            }
        }
    }

    private fun inflaterView(mLayoutId:Int,parent : ViewGroup):View {
        val view = mInflater?.inflate(mLayoutId,parent,false)
        return view?: View(parent.context)
    }


    private fun setVideoItem(holder: ViewHolder,item : HomeBean.Issue.Item) {
        val itemData = item.data
        val defAvatar = R.mipmap.default_avatar
        val cover = itemData?.cover?.feed
        var tagText : String? = "#"
        var avatar = itemData?.author?.icon

        //作者出为空 就显示获取提供者的信息
        if(avatar.isNullOrEmpty()){
            avatar = itemData?.provider?.icon
        }
        //加载封面图片
        GlideApp.with(mContext)
            .load(cover)
            .placeholder(R.drawable.placeholder_banner)
            .transition(DrawableTransitionOptions().crossFade())
            .into(holder.getView(R.id.iv_cover_feed))

        //如果提供者信息为空，就显示默认
        if(avatar.isNullOrEmpty()) {
            GlideApp.with(mContext)
                .load(defAvatar)
                .placeholder(R.mipmap.default_avatar).circleCrop()
                .transition(DrawableTransitionOptions().crossFade())
                .into(holder.getView(R.id.iv_avatar))
        }else {
            GlideApp.with(mContext)
                .load(avatar)
                .placeholder(R.mipmap.default_avatar).centerCrop()
                .transition(DrawableTransitionOptions().crossFade())
                .into(holder.getView(R.id.iv_avatar))
        }

        holder.setText(R.id.tv_title,itemData?.title?:"")

        itemData?.tags?.take(4)?.forEach{
            tagText += (it.name + "/")
        }

        val timeFormat = durationFormat(itemData?.duration)
        tagText += timeFormat
        holder.setText(R.id.tv_tag,tagText!!)
        holder.setText(R.id.tv_category,"#"+ itemData?.category)
        holder.setOnItemClickListener(listener = View.OnClickListener {
            goToVideoPlay(mContext as Activity,holder.getView(R.id.iv_cover_feed),item)
        })
    }

    /**
     * 跳转到视频播放页面详情
     */
    private fun goToVideoPlay(activity: Activity,view:View,itemData: HomeBean.Issue.Item){
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(Constants.BUNDLE_VIDEO_DATA,itemData)
        intent.putExtra(VideoDetailActivity.TRANSITION,true)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val pair = Pair(view, VideoDetailActivity.IMG_TRANSITION)
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, pair)
            ActivityCompat.startActivity(activity,intent,activityOptions.toBundle())
        }else {
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_in,R.anim.anim_out)
        }
    }

}