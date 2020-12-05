package com.interview.dynamiclist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.data.remote.WebSocketService
import com.interview.dynamiclist.data.remote.filter.FeedFilterAPIService
import com.interview.dynamiclist.data.remote.filter.FilterType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val webSocketService : WebSocketService) {
    private val _feedEvent =  MediatorLiveData<FeedEventModel>()
    val feedEvent : LiveData<FeedEventModel> get() = _feedEvent

    init {
        val feedEventApiSource: LiveData<FeedEventModel> = webSocketService.feedEventSocket
        _feedEvent.addSource(
            feedEventApiSource,
            Observer<FeedEventModel?> { feedEventModel -> _feedEvent.value = feedEventModel })
        FeedFilterAPIService.filter = FilterType.FEED_NO_LIMIT
    }

    fun startGetFeed() {
        webSocketService.connect()
    }

    fun stopGetFeed() {
        webSocketService.disconnect()
    }

    fun updateFilter(query : String) {
        FeedFilterAPIService.parseToFilterType(query)
    }
}