package kg.bakai.repotestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.bakai.repotestapp.data.remote.GithubApi
import kg.bakai.repotestapp.data.repository.SearchRepositoryImpl
import kg.bakai.repotestapp.domain.repository.SearchRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): GithubApi {
        val logging = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: GithubApi): SearchRepository {
        return SearchRepositoryImpl(api)
    }
}