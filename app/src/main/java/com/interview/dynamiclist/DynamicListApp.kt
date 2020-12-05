package com.interview.dynamiclist

import android.app.Application
import com.interview.dynamiclist.di.component.AppComponent
import com.interview.dynamiclist.di.component.DaggerAppComponent

class DynamicListApp : Application() {
   lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
    }

    /*fun getAppComponent(): AppComponent {
        return appComponent
    }*/
}