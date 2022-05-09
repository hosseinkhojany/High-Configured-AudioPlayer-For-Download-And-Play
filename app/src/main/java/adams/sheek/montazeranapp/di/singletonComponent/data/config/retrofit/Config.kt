package adams.sheek.montazeranapp.di.singletonComponent.data.config.retrofit

import adams.sheek.montazeranapp.data.config.retrofit.EndPoints
import adams.sheek.montazeranapp.data.config.retrofit.HttpRequestInterceptor
import adams.sheek.montazeranapp.data.config.retrofit.RestAdapter
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Config {


    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(RestAdapter.getUnsafeOkHttpClient().build())
            .baseUrl("https://www.montazeran-monji.ir/app/api/v1/")
            .addConverterFactory(Json{
                ignoreUnknownKeys = true
                coerceInputValues = true
            }.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitEndPoints(retrofit: Retrofit): EndPoints {
        return retrofit.create(EndPoints::class.java)
    }


}
