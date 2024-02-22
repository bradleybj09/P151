package com.iambenbradley.p151.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineScopeProvider {

    @ApplicationProcessScope
    @Singleton
    @Provides
    fun provideApplicationProcessJob(): Job = Job()

    @ApplicationProcessScope
    @Provides
    fun provideApplicationProcessCoroutineScope(
        @ApplicationProcessScope job: Job,
    ) = CoroutineScope(Dispatchers.Default + job)

}

/**
 * Warning! Check your scope. This coroutine scope should only be used with @Singleton or otherwise
 * application-lifespanned components. This job is connected to the SingletonComponent and *will*
 * leak whatever you're doing in it if your component were to end sooner than that.
 */
annotation class ApplicationProcessScope