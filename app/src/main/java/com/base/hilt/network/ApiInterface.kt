package com.base.hilt.network

import com.base.hilt.model.response.ChangePasswordResponse
import com.base.hilt.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

/**
 * Interface used for api
 */
@Singleton
interface ApiInterface {
    @GET("movieList")
    suspend fun getAllMovies(): Response<ResponseData<com.base.hilt.model.response.MovieListResponse>>

    @POST("login")
    suspend fun login(): Response<ResponseData<LoginResponse>>

    @POST("changePassword")
    suspend fun changePassword(): Response<ResponseData<ChangePasswordResponse>>

}
