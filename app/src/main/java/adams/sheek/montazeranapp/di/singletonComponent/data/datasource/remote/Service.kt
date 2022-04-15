package adams.sheek.montazeranapp.di.singletonComponent.data.datasource.remote

import adams.sheek.montazeranapp.data.config.retrofit.EndPoints
import adams.sheek.montazeranapp.data.datasource.remote.LoginService
import adams.sheek.montazeranapp.data.datasource.remote.TopicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Service {

    @Provides
    @Singleton
    fun provideLoginService(service: EndPoints): LoginService {
        return LoginService(service)
    }

    @Provides
    @Singleton
    fun provideTopicService(service: EndPoints): TopicService {
        return TopicService(service)
    }
}