package com.interview.dynamiclist.di.component

import android.app.Application
import com.interview.dynamiclist.di.module.FeedViewModelModule
import com.interview.dynamiclist.di.module.NetworkModule
import com.interview.dynamiclist.ui.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, FeedViewModelModule::class])
interface AppComponent {

    fun inject(mainFragment: MainFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}