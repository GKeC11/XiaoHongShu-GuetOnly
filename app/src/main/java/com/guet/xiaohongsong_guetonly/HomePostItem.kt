package com.guet.xiaohongsong_guetonly

import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.module_common.BaseBean
import com.example.module_common.BaseItem
import com.guet.common_module.BaseViewHolder
import kotlinx.android.synthetic.main.item_home_post.view.*
import org.greenrobot.eventbus.EventBus

class HomePostItem(context: Context) : BaseItem {
    override var context: Context = context

    override fun getLayoutId(): Int {
        return R.layout.item_home_post
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, bean: BaseBean) {
        val view = holder.root
        val postBean = bean as PostBean

        view.cv_post.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("imgUrl", postBean.imgUrl)
            bundle.putString("title", postBean.title)
            bundle.putString("desc", postBean.desc)
            bundle.putString("user", postBean.userName)
            bundle.putLong("id", postBean.id!!)

            EventBus.getDefault().post(DetailEvent(bundle))
        }
        view.iv_post.apply {
            Glide.with(context).load(postBean.imgUrl).into(this)
        }
        view.tv_desc.text = postBean.title
        view.tv_user_name.text = postBean.userName
        view.tv_like_num.text = postBean.likeNum.toString()

        if(StateUtil.userLikePostList!!.contains(postBean.id)){
            view.iv_like.setImageResource(R.drawable.ic_like)
        }else{
            view.iv_like.setImageResource(R.drawable.ic_unlike)
        }

        view.iv_like.setOnClickListener {
            PostHelper.likePost(postBean.id!!, object : LikeActionListener {
                override fun onLikeSucceed(isLike: Boolean) {
                    PostHelper.getUserLikePostId()
                    if (isLike) {
                        Glide.with(context).load(R.drawable.ic_like).into(view.iv_like)
                        postBean.likeNum = postBean.likeNum!! + 1
                        view.tv_like_num.text = (postBean.likeNum).toString()
                    } else {
                        Glide.with(context).load(R.drawable.ic_unlike).into(view.iv_like)
                        if(postBean.likeNum!! > 0){
                            postBean.likeNum = postBean.likeNum!! - 1
                        }
                        view.tv_like_num.text = (postBean.likeNum).toString()
                    }
                }
            })
        }

    }

}

interface LikeActionListener {
    fun onLikeSucceed(isLike: Boolean)
}