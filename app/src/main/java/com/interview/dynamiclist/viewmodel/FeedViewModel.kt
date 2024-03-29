package com.interview.dynamiclist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.data.repository.FeedRepository
import com.interview.dynamiclist.util.Resource
import javax.inject.Inject

class FeedViewModel @Inject constructor (val feedRepository: FeedRepository) : ViewModel() {

    val feedActionState = MutableLiveData<Boolean>()

    init {
        feedActionState.value = false
    }

    fun getFeedActionState() : LiveData<Boolean> {
        return feedActionState
    }

    fun getFeedItem(): LiveData<Resource<FeedEventModel>> = feedRepository.feedEvent

    fun startRunFeeds() {
        feedRepository.startGetFeed()
        feedActionState.value = true
    }

    fun stopRunFeeds() {
        feedRepository.stopGetFeed()
        feedActionState.value = false
    }

    fun updateFilter(query : String) {
        feedRepository.updateFilter(query)
    }

}