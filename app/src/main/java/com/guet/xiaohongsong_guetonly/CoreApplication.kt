package com.guet.xiaohongsong_guetonly

import android.app.Application
import cn.bmob.v3.Bmob
import com.example.module_common.LayoutCenter
import com.guet.common_module.SharedPreferencesUtil

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /*
        * 初始化BaseBean和BaseItem的联系
        * */
        LayoutCenter.init(this)
        LayoutCenter.register(PostBean::class.java.simpleName, HomePostItem::class.java.name)

        /*
        * 初始化BmobSDK
        * */
        Bmob.initialize(this,"293b241c6845f5cdc8cf27c68fd65d97")

        /*
        * 初始化Util
        * */
        SharedPreferencesUtil.init(this, getString(R.string.app_id))

    }
}