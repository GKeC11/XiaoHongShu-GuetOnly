package com.guet.xiaohongsong_guetonly

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.google.android.material.snackbar.Snackbar
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.Bmob.Post
import org.greenrobot.eventbus.EventBus

class PostHelper {
    companion object{
        fun getMD5(repStr: String?): String{
            val begin = repStr!!.indexOf("MD5: ") + 5
            val end = repStr.indexOf("</h1>")
            val md5 = repStr.substring(begin, end)

            return md5
        }

        fun addToBmob(view: View?, repStr: String?, title: String, desc: String){
            val md5 = getMD5(repStr)
            val imgUrl = "http://175.24.32.115:4869/" + md5
            val post = Post()
            post.title = title
            post.desc = desc
            post.userName = BmobUser.getCurrentUser(AppUser::class.java).username
            post.imgUrl = imgUrl
            post.likeNum = 0
            post.save(object : SaveListener<String>(){
                override fun done(objectId: String?, e: BmobException?) {
                    if(e == null){
                        Snackbar.make(view!!, "发布成功", Snackbar.LENGTH_LONG).show()
                    } else {
                        Log.e("BMOB", e.toString())
                        Snackbar.make(view!!, "发布失败", Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }

        fun getPost(view: View?){
            getUserLikePostId()
            val bmobQuery = BmobQuery<Post>()
            bmobQuery.findObjects(object : FindListener<Post>(){
                override fun done(posts: MutableList<Post>?, e: BmobException?) {
                    if (e == null) {
                        EventBus.getDefault().post(PostEvent(posts))
                    } else {
                        Log.e("BMOB", e.toString());
                        Snackbar.make(view!!, "获取数据失败", Snackbar.LENGTH_SHORT)
                    }
                }

            })
        }

        fun getPostUserLike(view: View?){
            getUserLikePostId()
            val bmobQuery = BmobQuery<Post>()
            bmobQuery.findObjects(object : FindListener<Post>(){
                override fun done(posts: MutableList<Post>?, e: BmobException?) {
                    if (e == null) {
                        val postsUserLike =  mutableListOf<Post>()
                        posts?.forEach {
                            if(StateUtil.userLikePostList!!.contains(it.id)){
                                postsUserLike.add(it)
                            }
                        }
                        EventBus.getDefault().post(PostEvent(postsUserLike))
                    } else {
                        Log.e("BMOB", e.toString());
                        Snackbar.make(view!!, "获取数据失败", Snackbar.LENGTH_SHORT)
                    }
                }

            })
        }

        fun getUserLikePostId(){
            val bmobQuery = BmobQuery<AppUser>()
            bmobQuery.addWhereEqualTo("id", StateUtil.user?.id)
            bmobQuery.findObjects(object : FindListener<AppUser>(){
                override fun done(userList: MutableList<AppUser>?, e: BmobException?) {
                    if (e == null){
                        StateUtil.userLikePostList = userList?.get(0)?.likePostId
                        Log.d("Test", "get post user like")
                    } else {
                        Log.e("BMOB", e.toString())
                    }
                }

            })
        }

        fun likePost(postId: Long, actionListener: LikeActionListener){
            val user = StateUtil.user!!
            var isLike: Boolean

            if (user.likePostId == null){
                user.likePostId = mutableListOf()
                user.likePostId?.add(postId)
                isLike = true
            }
            else if(!user.likePostId!!.contains(postId)){
                user.likePostId?.add( postId)
                isLike = true
            } else {
                user.likePostId?.remove(postId)
                isLike = false
            }

            // 更新帖子状态
            updatePost(postId, isLike)

            // 严格来说 应该确认用户数据和帖子数据的更新是否都成功，功能耗时较长置后
            user.update(StateUtil.user?.objectId, object : UpdateListener(){
                override fun done(e: BmobException?) {
                    if (e==null) {
                        Log.d("Test", "like update succeed")
                        actionListener.onLikeSucceed(isLike)
                    } else {
                        Log.d("BMOB", "like action err" + e.toString())
                    }
                }
            })
        }

        /* 严格来讲帖子数据的更新不应放在客户端
         * 但是Bmob仅仅是云数据库而已
         * 所以更新服务器中帖子数据的逻辑写在客户端 */
        fun updatePost(postId: Long, isLike: Boolean){
            var postQuery = BmobQuery<Post>()
            postQuery.addWhereEqualTo("id", postId)
            postQuery.findObjects(object : FindListener<Post>(){
                override fun done(posts: MutableList<Post>?, e: BmobException?) {
                    if(e == null)
                    {
                        /* 更新数据 */
                        var post = posts?.get(0)
                        if(isLike){
                            post!!.likeNum = post?.likeNum!! + 1
                        } else if(post?.likeNum != 0L){
                            post!!.likeNum = post?.likeNum!! - 1
                        }

                        post?.update(post.objectId, object : UpdateListener(){
                            override fun done(e: BmobException?) {
                                if(e == null)
                                {
                                    Log.d("PostHelper", "Post Update Success")
                                }
                            }

                        })
                    }

                }
            })
        }

    }

}

class PostEvent(posts: MutableList<Post>?){
    var posts = posts
}