package com.interview.dynamiclist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.data.remote.WebSocketService
import com.interview.dynamiclist.data.remote.filter.FeedFilterAPIService
import com.interview.dynamiclist.data.remote.filter.FilterType
import com.interview.dynamiclist.util.Resource
import com.interview.dynamiclist.util.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val webSocketService : WebSocketService) {
    private val _feedEvent =  MediatorLiveData<Resource<FeedEventModel>>()
    val feedEvent : LiveData<Resource<FeedEventModel>> get() = _feedEvent

    init {
        val feedEventApiSource: LiveData<Resource<FeedEventModel>> = webSocketService.feedEventSocket
        _feedEvent.addSource(
            feedEventApiSource,
            Observer<Resource<FeedEventModel>> {
                    //feedEventModel -> _feedEvent.value = feedEventModel
                when (it.status) {
                    Status.SUCCESS -> {
                        it.apply { _feedEvent.value = it}
                    }
                    Status.ERROR -> {
                        /* here we need check if code is 400 and there is no close contain in the
                        error message and then schedule workmanager to try reconnect

                        */

                    }
                }
            })
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