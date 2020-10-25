package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.bmob.v3.BmobUser
import com.bumptech.glide.Glide
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    var binding: FragmentDetailBinding? = null
    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        StateUtil.action = StateUtil.OTHER_FRAGMENT
        bundle = arguments

        Glide.with(requireContext())
            .load(R.drawable.ic_cat)
            .circleCrop()
            .into(binding!!.ivAvatar)

        binding?.tbDetail?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding?.tvTitle?.text = bundle?.getString("title")
        binding?.tvDesc?.text = bundle?.getString("desc")
        binding?.tvUserName?.text = bundle?.getString("user")
        Glide.with(requireContext()).load(bundle?.getString("imgUrl") + "?p=0").into(binding!!.ivImage)
    }
}

class DetailEvent(bundle: Bundle){
    val bundle = bundle
}