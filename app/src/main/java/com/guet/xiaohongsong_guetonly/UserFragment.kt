package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.bmob.v3.BmobUser
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.example.module_common.BaseBeanAdapter
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.databinding.FragmentUserBinding
import kotlinx.android.synthetic.main.fragment_user.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserFragment : MainFragment() {
    var binding: FragmentUserBinding? = null
    var postAdapter = BaseBeanAdapter<PostBean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)

        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        Glide.with(requireContext())
            .load(R.drawable.ic_cat)
            .circleCrop()
            .into(binding!!.ivAvatar)

        binding?.tvUserName?.text = BmobUser.getCurrentUser(AppUser::class.java).username

        PostHelper.getPostUserLike(view)

        binding?.rvPost?.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = postAdapter
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPullSucceed(event: PostEvent){
        val postBeans = mutableListOf<PostBean>()
        event.posts?.forEach {
            val post = PostBean(it.id, it.title, it.desc, it.userName, it.imgUrl, it.likeNum)
            postBeans.add(post)
        }
        postAdapter.addBeans(postBeans)
    }
}