package com.iambenbradley.p151.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn

@Module
@InstallIn(SingletonComponent::class)
class NetworkMonitorProviderModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(connectivityManager: ConnectivityManager): NetworkMonitor {
        return NetworkMonitorImpl(connectivityManager)
    }
}

interface NetworkMonitor {
    val networkAvailable: Flow<Boolean>
}

@Singleton
class NetworkMonitorImpl @Inject constructor(
    connectivityManager: ConnectivityManager,
) : NetworkMonitor {

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
        this == null -> false
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
            (
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) -> true
        else -> false
    }

    override val networkAvailable: Flow<Boolean> = callbackFlow {
        trySendBlocking(
            connectivityManager
                .getNetworkCapabilities(connectivityManager.activeNetwork)
                .isNetworkCapabilitiesValid(),
        )

        val builder = NetworkRequest.Builder()
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.wtf("bbradley", "1")
                trySendBlocking(true)
            }

            override fun onLost(network: Network) {
                Log.wtf("bbradley", "2")
                trySendBlocking(false)
            }
        }
        connectivityManager.registerNetworkCallback(builder.build(), connectivityCallback)
        awaitClose { connectivityManager.unregisterNetworkCallback(connectivityCallback) }
    }.shareIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, replay = 1)
}
