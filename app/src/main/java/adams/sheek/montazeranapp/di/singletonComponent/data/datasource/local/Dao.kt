package adams.sheek.montazeranapp.di.singletonComponent.data.datasource.local

import adams.sheek.montazeranapp.data.config.room.AppDatabase
import adams.sheek.montazeranapp.data.datasource.local.TopicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Dao {

    @Provides
    @Singleton
    fun provideTopicDao(appDatabase: AppDatabase): TopicDao {
        return appDatabase.topicDao()
    }

}