package com.appname.structure.user.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appname.structure.utils.MainCoroutineRule
import com.base.hilt.model.response.MovieListResponse
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.home.HomeRepository
import com.base.hilt.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the [HomeRepository].
 */
class MainRepositoryTest {

    lateinit var mainRepository: HomeRepository
    lateinit var homeViewModel: HomeViewModel
    private var baseDataResponse = MovieListResponse()

    @Mock
    lateinit var apiService: ApiInterface

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
//        Dispatchers.setMain(testDispatcher)
        mainRepository = HomeRepository(apiService)
        homeViewModel = HomeViewModel(mainRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `movie test`() {
        runTest {
         /* doReturn(baseDataResponse)
                .`when`(apiService)
                .getAllMovies()*/
            homeViewModel.callHomeScreenAPI()
            delay(2000)
            val expected = listOf(
                ResponseHandler.Loading,
                ResponseHandler.OnSuccessResponse(baseDataResponse)
            )
        /*    val result =
                homeViewModel.responseLiveHomeVendorListResponse.getOrAwaitValue() as ResponseHandler.OnSuccessResponse<ResponseData<MovieListResponse>?>
//                homeViewModel.responseLiveHomeVendorListResponse.getOrAwaitValue() as ResponseHandler.Loading

            Assert.assertTrue(expected[1]::class == result::class)*/
        }

    }


/*    internal class FakeDemoRepository : FakeHomeRepository {
        override suspend fun getMovie(forceRefresh: Boolean): List<Movie> = TestData.movieList

    }*/
}