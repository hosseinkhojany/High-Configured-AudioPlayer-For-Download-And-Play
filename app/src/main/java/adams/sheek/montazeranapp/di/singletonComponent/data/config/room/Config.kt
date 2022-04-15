package adams.sheek.montazeranapp.di.singletonComponent.data.config.room

import adams.sheek.montazeranapp.data.config.room.AppDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Config {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext application: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}