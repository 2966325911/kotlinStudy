package com.kangengine.kotlinstudydemo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.kangengine.kotlinstudydemo.BuildConfig
import com.kangengine.kotlinstudydemo.Constants
import com.kangengine.kotlinstudydemo.R
import com.kangengine.kotlinstudydemo.durationFormat
import com.kangengine.kotlinstudydemo.glide.GlideApp
import com.kangengine.kotlinstudydemo.ui.activity.VideoDetailActivity
import com.kangengine.kotlinstudydemo.view.recyclerview.ViewHolder
import com.kangengine.kotlinstudydemo.view.recyclerview.adapter.CommonAdapter
import com.tencent.bugly.proguard.ac
import kotlinx.android.synthetic.main.item_home_content.view.*

/**
 *
 *   @author : Vic
 *   time    : 2018-12-25 21:19
 *   desc    : 水平的RecycleViewAdapter
 *
 */
class FollowHorizontalAdapter(mContext : Context,categoryList:ArrayList<HomeBean.Issue.Item>,layoutId:Int):
        CommonAdapter<HomeBean.Issue.Item>(mContext,categoryList,layoutId) {

    /**
     * 绑定数据
     */
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        val horizontalItemData = data.data
        holder.setImagePath(R.id.iv_cover_feed,object:ViewHolder.HolderImageLoader(data.data?.cover?.feed!!){
            override fun loadImage(iv: ImageView, path: String) {
                //加载封面页
                GlideApp.with(mContext)
                    .load(path)
                    .placeholder(R.drawable.placeholder_banner)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(holder.getView(R.id.iv_cover_feed))
            }
        })

        //横向RecyclerView封面图下面标题
        holder.setText(R.id.tv_title,horizontalItemData?.title?:"")

        val timeFormat = durationFormat(horizontalItemData?.duration)
        with(holder) {
            if(horizontalItemData?.tags != null && horizontalItemData.tags.size > 0) {
                setText(R.id.tv_tag,"#${horizontalItemData.tags[0].name}/$timeFormat")
            }else {
                setText(R.id.tv_tag,"#$timeFormat")
            }


            setOnItemClickListener(listener = View.OnClickListener {
                goToVideoPlayer(mContext as Activity,holder.getView(R.id.iv_cover_feed),data)
            })
        }
    }

    /**
     * 跳转到视频详情页面播放
     */
    private fun goToVideoPlayer(activity: Activity,view : View,itemData:HomeBean.Issue.Item) {
        val intent = Intent(activity,VideoDetailActivity::class.java)
        intent.putExtra(Constants.BUNDLE_VIDEO_DATA,itemData)
        intent.putExtra(VideoDetailActivity.Companion.TRANSITION,true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val pair = Pair<View,String>(view,VideoDetailActivity.IMG_TRANSITION)
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair)

            ActivityCompat.startActivity(activity,intent,activityOptions.toBundle())
        } else {
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_in,R.anim.anim_out)
        }
    }


}
