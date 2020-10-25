package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.google.android.material.snackbar.Snackbar
import com.guet.common_module.SharedPreferencesUtil
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        binding?.etUserName?.setText(SharedPreferencesUtil.getUserName())
        binding?.etPassword?.setText(SharedPreferencesUtil.getPassword())

        binding?.cvRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding?.cvLogin?.setOnClickListener {
            login()
        }
    }

    fun logining(){
        binding?.tvLogin?.visibility = View.INVISIBLE
        binding?.pbLogin?.visibility = View.VISIBLE
        binding?.cvLogin?.isClickable = false
    }

    fun canLogin(){
        binding?.tvLogin?.visibility = View.VISIBLE
        binding?.pbLogin?.visibility = View.INVISIBLE
        binding?.cvLogin?.isClickable = true
    }

    fun login(){
        val user = AppUser()
        val username = binding?.etUserName?.text.toString()
        val password = binding?.etPassword?.text.toString()

        user.username = username
        user.setPassword(password)
        logining()

        user.login(object : SaveListener<AppUser>(){
            override fun done(user: AppUser?, e: BmobException?) {
                if (e == null) {
                    Snackbar.make(view!!, user?.getUsername() + "欢迎回来", Snackbar.LENGTH_LONG).show()
                    SharedPreferencesUtil.saveLoginInfo(username, password)

                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    Snackbar.make(view!!, "用户名或密码错误", Snackbar.LENGTH_LONG).show()
                    activity?.runOnUiThread {
                        canLogin()
                    }
                }
            }
        })
    }
}