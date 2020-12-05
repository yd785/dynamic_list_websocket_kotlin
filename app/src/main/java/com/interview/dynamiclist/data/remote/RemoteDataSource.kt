package com.interview.dynamiclist.data.remote

interface RemoteDataSource {
    fun connect()
    fun disconnect()
}