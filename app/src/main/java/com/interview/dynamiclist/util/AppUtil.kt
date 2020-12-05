package com.interview.dynamiclist.util

import android.net.Uri
import okhttp3.HttpUrl

class AppUtil {
    companion object {
        fun getSocketUrl(): String? {
            val builder = Uri.Builder()
                .scheme(Constants.SOCKET_SCHEME)
                .authority(Constants.HOST_URL)
                .build()
            return "$builder/receive"
        }

        fun getHttpUrl(): String? {
            val httpUrl = HttpUrl.Builder()
                .scheme(Constants.HTTP_SCHEME)
                .host(Constants.HOST_URL)
                .build()
            return "$httpUrl/"
        }

        fun isNumber(str: String): Boolean {
            for (ch in str.toCharArray()) {
                if (!Character.isDigit(ch)) return false
            }
            return true
        }
    }
}