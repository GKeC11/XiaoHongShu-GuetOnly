package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.bmob.v3.BmobUser
import com.example.module_common.BaseBeanAdapter
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.databinding.FragmentHomeBinding
import okhttp3.MultipartBody
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*


class HomeFragment : MainFragment() {
    var binding: FragmentHomeBinding? = null;
    var postAdapter = BaseBeanAdapter<PostBean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        PostHelper.getPost(view)
        StateUtil.user = BmobUser.getCurrentUser(AppUser::class.java)

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClickPost(event: DetailEvent){
        findNavController().navigate(R.id.action_global_detailFragment, event.bundle)
    }
}