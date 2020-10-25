package com.guet.xiaohongsong_guetonly

import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import com.guet.xiaohongsong_guetonly.Bmob.AppUser

class StateUtil {
    companion object {
        val MAIN_FRAGMENT = 0
        val OTHER_FRAGMENT = 1

        var action = MAIN_FRAGMENT
        var user: AppUser? = null
        var userLikePostList: MutableList<Long>? = mutableListOf()
    }
}

class StateChangeEvent(isMainActivity: Boolean){

    var isMainActivity  = isMainActivity

}