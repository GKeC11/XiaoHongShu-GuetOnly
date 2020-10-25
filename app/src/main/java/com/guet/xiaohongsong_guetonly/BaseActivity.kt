package com.guet.xiaohongsong_guetonly

import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus




open class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}