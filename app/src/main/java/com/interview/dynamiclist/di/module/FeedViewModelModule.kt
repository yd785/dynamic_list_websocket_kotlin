package com.interview.dynamiclist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.interview.dynamiclist.viewmodel.FeedViewModel
import com.interview.dynamiclist.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class FeedViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindsFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}