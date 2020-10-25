package com.example.module_common

import android.content.Context
import com.guet.common_module.BaseViewHolder

interface BaseItem {
    var context: Context
    fun getLayoutId(): Int
    fun onBindViewHolder(holder: BaseViewHolder, position: Int, bean: BaseBean)
}