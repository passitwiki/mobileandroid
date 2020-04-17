package com.passitwiki.passit.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

/**
 * An application class that starts Koin in the context of the whole app,
 * defines modules.
 */
class PassIt : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PassIt)
            fragmentFactory()
            modules(appModule)
        }
    }
}