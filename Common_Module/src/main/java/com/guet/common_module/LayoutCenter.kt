package com.example.module_common

import android.content.Context

/*
*
* 使用方法：
* 新建基于 BaseBean 的数据类
* 新建基于 BaseItem 的界面类
* 使用 BaseBeanAdapter<> 作为适配器
* 使用该类注册 Bean 与 Item 的关系
*
**/

class LayoutCenter {

    companion object{
        var context: Context? = null
        var bean2ItemMap = hashMapOf<String, String>()

        fun init(context: Context){
            Companion.context = context
        }

        fun build(bean: BaseBean): BaseItem {
            val itemName = bean2ItemMap.get(bean.javaClass.simpleName)
            val item = Class.forName(itemName)
            return item.getConstructor(android.content.Context::class.java).newInstance(
                context
            ) as BaseItem
        }

        fun register(bean: String, item: String){
            bean2ItemMap.put(bean, item)
        }
    }

}