package com.guet.xiaohongsong_guetonly.Bmob

import cn.bmob.v3.BmobObject

class Post : BmobObject() {
    var id: Long? = null
    var title: String? = null
    var desc: String? = null
    var userName: String? = null
    var imgUrl: String? = null
    var likeNum: Long? = null
}