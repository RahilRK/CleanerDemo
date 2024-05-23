package com.appname.structure.user.di

import com.base.hilt.ConfigFiles
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class TestAppModule {

    @Provides
    @Named("Normal")
    fun getRetrofit(@Named("NormalOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        /*var objectMapper=ObjectMapper()
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)*/
        val builder = Retrofit.Builder()
        builder.baseUrl(ConfigFiles.DEV_BASE_URL)
        builder.addConverterFactory(JacksonConverterFactory.create())
        builder.client(okHttpClient)
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("OkHttpClient")
    fun provideOkHttpClient(
        @Named("MockInterceptor") authInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
//        .addInterceptor(OkHttpProfilerInterceptor())
        .addInterceptor(authInterceptor)
        .build()


}