package adams.sheek.montazeranapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this
    }



    companion object {
        lateinit var instance: App
            private set
        fun context(): Context = instance.applicationContext
    }
}