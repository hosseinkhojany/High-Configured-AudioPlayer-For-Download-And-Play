package adams.sheek.montazeranapp.di.viewModelComponent

import adams.sheek.montazeranapp.data.datasource.local.TopicDao
import adams.sheek.montazeranapp.data.datasource.remote.LoginService
import adams.sheek.montazeranapp.data.datasource.remote.TopicService
import adams.sheek.montazeranapp.data.repositories.LoginRepository
import adams.sheek.montazeranapp.data.repositories.TopicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideLogin(
        service: LoginService,
        coroutineDispatcher: CoroutineDispatcher
    ): LoginRepository {
        return LoginRepository(service, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun provideUsers(
        service: TopicService,
        dao: TopicDao,
        coroutineDispatcher: CoroutineDispatcher
    ): TopicRepository {
        return TopicRepository(service, dao, coroutineDispatcher)
    }

}