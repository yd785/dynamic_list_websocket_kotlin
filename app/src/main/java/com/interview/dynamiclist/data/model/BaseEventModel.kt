package com.interview.dynamiclist.data.model

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

open class BaseEventModel {
    companion object {
        open fun <T : BaseEventModel?> fromJsonToObject(
            gson: Gson,
            jsonStr: String?,
            typeOf: Class<T>?
        ): T {
            return gson.fromJson(jsonStr, typeOf)
        }

        open fun toJsonFromObject(gson: Gson): JSONObject? {
            try {
                return JSONObject(gson.toJson(this))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }

    }



}