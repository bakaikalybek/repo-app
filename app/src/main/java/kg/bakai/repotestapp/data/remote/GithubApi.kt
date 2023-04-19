package kg.bakai.repotestapp.data.remote

import kg.bakai.repotestapp.data.remote.dto.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): Response<SearchResult>

    @GET("/search/repositories")
    suspend fun searchRepositoriesRes(
        @Query("q") query: String,
        @Query("per_page") size: Int,
        @Query("page") page: Int
    ): Response<SearchResult>
}