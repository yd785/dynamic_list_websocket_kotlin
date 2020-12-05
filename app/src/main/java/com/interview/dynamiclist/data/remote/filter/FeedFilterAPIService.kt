package com.interview.dynamiclist.data.remote.filter

import android.util.Log
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.util.AppUtil
import java.util.regex.Matcher
import java.util.regex.Pattern

interface Filterable {
    fun isMatchType(feed: FeedEventModel): Boolean
}

fun String.isRGBColor(str : String) : Boolean {
    val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
    val p: Pattern = Pattern.compile(regex)
    if (str == null)
        return false

    val m: Matcher = p.matcher(str)
    return m.matches()
}

object FeedFilterAPIService : Filterable {
    private const val TAG = "FeedFilterAPIService"
    
    lateinit var filter : FilterType
    lateinit var query : String

    @Synchronized fun parseToFilterType(queryValue : String) {
        query = queryValue
        filter = when {
            query.toDoubleOrNull() != null -> FilterType.FEED_FROM_WEIGHT
            query.isRGBColor(query) -> FilterType.FEED_COLOR
            else -> FilterType.FEED_NO_LIMIT
        }
        Log.d(TAG, "parseToFilterType: query: $query")
        Log.d(TAG, "parseToFilterType: filter value: ${filter.value}")
    }

    override fun isMatchType(feed: FeedEventModel): Boolean {
        return when (filter) {
            FilterType.FEED_FROM_WEIGHT -> {
                Log.d(TAG, "isMatchType: ${filter.value}")

                val feedWeight: Double =
                    feed.weight.substring(0, feed.weight.length - 2)
                        .toDouble()
                val queryNum = query.toDoubleOrNull()
                Log.d(TAG, "isMatchType: feedweight: $feedWeight > queryweight ${queryNum} : ${feedWeight >= queryNum!!}")
                queryNum != null && queryNum <= feedWeight
            }
            FilterType.FEED_COLOR -> {feed.color == query}
            FilterType.FEED_NO_LIMIT -> {true}
            else -> false
        }
    }
}


