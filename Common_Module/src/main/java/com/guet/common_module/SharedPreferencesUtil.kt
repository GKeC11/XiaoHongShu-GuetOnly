package com.guet.common_module

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil {

    companion object{
        var context: Context? = null
        var sharedPreferences: SharedPreferences? = null

        fun init(context: Context, appId: String){
            this.context = context
            sharedPreferences = context?.getSharedPreferences(appId, Context.MODE_PRIVATE)
        }

        fun saveLoginInfo(username: String, password: String){
            sharedPreferences?.edit()?.apply {
                putString("userName", username)
                putString("password", password)

                commit()
            }
        }

        fun getUserName(): String?{
            return sharedPreferences?.getString("userName","")
        }

        fun getPassword(): String?{
            return sharedPreferences?.getString("password","")
        }
    }
}