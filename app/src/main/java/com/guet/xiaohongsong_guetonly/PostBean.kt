package com.guet.xiaohongsong_guetonly

import com.example.module_common.BaseBean

class PostBean(id: Long?, title: String?, desc: String?, userName: String?, imgUrl: String?, likeNum: Long?) : BaseBean {
    var id = id
    var title = title
    var desc = desc
    var userName = userName
    var imgUrl = imgUrl
    var likeNum = likeNum
}