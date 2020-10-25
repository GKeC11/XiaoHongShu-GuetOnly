package com.guet.xiaohongsong_guetonly.Bmob

import cn.bmob.v3.BmobUser

class AppUser : BmobUser() {
    var id: Integer? = null
    var likePostId: MutableList<Long>? = null
}