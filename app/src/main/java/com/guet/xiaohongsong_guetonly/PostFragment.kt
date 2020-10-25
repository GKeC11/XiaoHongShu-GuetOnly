package com.guet.xiaohongsong_guetonly

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.guet.common_module.FileUtil
import com.guet.xiaohongsong_guetonly.databinding.FragmentPostBinding
import okhttp3.*
import java.io.File
import java.io.IOException


class PostFragment : Fragment() {
    var binding: FragmentPostBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        val imgUri = arguments?.getString("imgUri")
        Glide.with(requireContext()).load(Uri.parse(imgUri)).into(binding!!.ivSelect)

        binding?.cvPost?.setOnClickListener {
            posting()
            upload(imgUri!!)
        }
    }

    fun posting(){
        binding?.tvPublish?.visibility = View.INVISIBLE
        binding?.pbPublish?.visibility = View.VISIBLE
        binding?.cvPost?.isClickable = false
    }

    fun canPost(){
        binding?.tvPublish?.visibility = View.VISIBLE
        binding?.pbPublish?.visibility = View.INVISIBLE
        binding?.cvPost?.isClickable = true
    }

    fun upload(uri: String){
        val title = binding?.etTitle?.text.toString()
        val desc = binding?.etDesc?.text.toString()
        if(title.length < 5 && desc.length < 5){
            Snackbar.make(binding?.cvPost as View, "标题或正文不能为空", Snackbar.LENGTH_SHORT)
            return
        }
        /*
        * 上传图片逻辑
        * */
        val path = FileUtil.getPathFromUri(requireContext(), Uri.parse(uri))
        val fileName = path?.split("/".toRegex())?.last()
        val file = File(path)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", fileName, RequestBody.create(MediaType.parse("image/*"), file))
            .build()

        val request = Request.Builder()
            .url("http://175.24.32.115:4869/upload")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Test", "onFailure: " + e.message)
                activity?.runOnUiThread {
                    canPost()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val rep = response.body()?.string()
                Log.d("Test", "onResponse: " + rep)

                PostHelper.addToBmob(binding?.cvPost as View, rep, title, desc)
                findNavController().popBackStack()
            }
        })
    }
}
