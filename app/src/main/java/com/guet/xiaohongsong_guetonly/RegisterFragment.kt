package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.google.android.material.snackbar.Snackbar
import com.guet.xiaohongsong_guetonly.Bmob.AppUser
import com.guet.xiaohongsong_guetonly.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        binding?.cvRegister?.setOnClickListener {
            register()
        }
    }

    fun register(){
        var isValid = false
        val user = AppUser()
        val username = binding?.etUserName?.text.toString()
        val password = binding?.etPassword?.text.toString()
        val confirm = binding?.etConfirm?.text.toString()

        user.username = username
        if(password.equals(confirm)) {
            user.setPassword(binding?.etPassword?.text.toString())
            isValid = true
        } else {
            Snackbar.make(binding?.cvRegister as View, "两次输入的密码不相同", Snackbar.LENGTH_LONG).show()
            isValid = false
        }

        if(isValid){
            user.signUp(object : SaveListener<AppUser>() {

                override fun done(user: AppUser?, e: BmobException?) {
                    if(e == null){
                        Snackbar.make(view!!, "注册成功", Snackbar.LENGTH_LONG).show()

                        findNavController().popBackStack()
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        Snackbar.make(view!!, "用户名或密码不能为空", Snackbar.LENGTH_LONG).show()
                    }
                }

            })
        }

    }
}