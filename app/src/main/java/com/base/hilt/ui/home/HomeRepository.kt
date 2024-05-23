package com.base.hilt.ui.home

import com.base.hilt.base.BaseRepository
import com.base.hilt.model.response.Movie
import com.base.hilt.model.response.MovieListResponse
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import javax.inject.Inject

/**
 * Single point of access to agenda data for the presentation layer.
 */
interface FakeHomeRepository {
    /**
     * Returns a list of [Movie]s. When the parameter is passed as true,
     * it's guaranteed the data loaded from this use case is up to date with the
     * remote data source (Remote Config)
     */
    suspend fun getMovie(forceRefresh: Boolean): List<Movie>
}
open class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    open suspend fun callHomeScreenAPI(): ResponseHandler<ResponseData<MovieListResponse>?> {
        return makeAPICall {
            apiInterface.getAllMovies()
        } //pass true if use retry network call functionality with attempt else not required any param.

    }
}