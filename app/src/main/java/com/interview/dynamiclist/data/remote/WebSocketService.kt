package com.interview.dynamiclist.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.interview.dynamiclist.data.model.BaseEventModel
import com.interview.dynamiclist.data.model.BaseEventModel.Companion.fromJsonToObject
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.data.remote.filter.FeedFilterAPIService
import com.interview.dynamiclist.util.AppUtil
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor (val feedEventClient : OkHttpClient, val gson : Gson) : WebSocketListener(), RemoteDataSource {
    private val  TAG = "WebSocketService"

    private val _feedEventSocket = MutableLiveData<FeedEventModel>()
    val feedEventSocket:LiveData<FeedEventModel>
       get() = _feedEventSocket

    private var isConnected = false
    lateinit var webSocket: WebSocket

    override fun connect() {
        Log.d(TAG, "connect: " + AppUtil.getSocketUrl())
        val request: Request? = AppUtil.getSocketUrl()?.let { Request.Builder().url(it).build() }
        Log.d(TAG, "connect: $request")
        webSocket = feedEventClient.newWebSocket(request!!, this)
    }

    override fun disconnect() {
        Log.d(TAG, "disconnect: ")
        webSocket?.close(1000, "Done using")
        webSocket?.cancel()
        isConnected = false
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "onClosed: ")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code$reason")
    }

    override fun onFailure(
        webSocket: WebSocket,
        t: Throwable,
        response: Response?
    ) {
        super.onFailure(webSocket, t, response)
        Log.d(TAG, "onFailure: " + response + webSocket + t.message)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "onMessage: ")
        handleMessage(text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d(TAG, "onOpen: $response")
        isConnected = true
    }

    @Synchronized
    private fun handleMessage(message: String) {
        Log.d(TAG, "handleMessage: $message")
        val feedEventModel = BaseEventModel.fromJsonToObject(
            gson, message,
            FeedEventModel::class.java
        )
        if (isConnected && FeedFilterAPIService.isMatchType(feedEventModel)) {
            Log.d(TAG, "handleMessage: match pass for feed weight ${feedEventModel.weight}")
            _feedEventSocket.postValue(feedEventModel)
        }

    }

}