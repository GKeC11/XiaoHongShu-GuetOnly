package com.example.module_common

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenHelper {
    companion object{

        fun open( context:Context, uri: String){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            context?.startActivity(intent)
        }
    }
}