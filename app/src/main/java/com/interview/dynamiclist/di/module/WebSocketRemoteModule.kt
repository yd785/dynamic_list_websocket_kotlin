package com.interview.dynamiclist.di.module

import com.interview.dynamiclist.data.remote.RemoteDataSource
import com.interview.dynamiclist.data.remote.WebSocketService
import dagger.Binds
import dagger.Module

@Module
abstract class WebSocketRemoteModule {
    @Binds
    abstract fun bindsWebSocketRemoteDataSource(webSocketService: WebSocketService): RemoteDataSource
}