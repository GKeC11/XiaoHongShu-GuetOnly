package com.guet.xiaohongsong_guetonly

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

open class MainFragment : Fragment() {
    var useEventBus = true

    override fun onStart() {
        super.onStart()
        StateUtil.action = StateUtil.MAIN_FRAGMENT

        if(useEventBus){
            EventBus.getDefault().register(this)
        }
    }

    override fun onResume() {
        super.onResume()

        EventBus.getDefault().post(
            StateChangeEvent(
                true
            )
        )
    }

    override fun onStop() {
        super.onStop()

        if(StateUtil.action != 0){
            EventBus.getDefault().post(
                StateChangeEvent(
                    false
                )
            )
        }

        EventBus.getDefault().unregister(this)
    }
}