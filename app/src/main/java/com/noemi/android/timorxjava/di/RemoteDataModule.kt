package com.noemi.android.timorxjava.di

import com.noemi.android.timorxjava.flickr.remote_data.RemoteAPI
import com.noemi.android.timorxjava.flickr.remote_data.RemoteAPISource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataModule {

    @Binds
    @Singleton
    abstract fun bind(remoteAPISource: RemoteAPISource) : RemoteAPI
}