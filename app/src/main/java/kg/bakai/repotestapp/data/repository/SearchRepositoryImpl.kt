package kg.bakai.repotestapp.data.repository

import android.util.Log
import kg.bakai.repotestapp.data.mapper.toRepository
import kg.bakai.repotestapp.data.remote.GithubApi
import kg.bakai.repotestapp.domain.model.Repository
import kg.bakai.repotestapp.domain.repository.SearchRepository
import kg.bakai.repotestapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api: GithubApi
) : SearchRepository {

    override suspend fun searchRepositories(q: String): Flow<Resource<List<Repository>>> {
        return flow {
            emit(Resource.Loading())
            val result = api.searchRepositories(query = q)
            if (result.isSuccessful) {
                val data = result.body()
                if (data != null) {
                    emit(Resource.Success(data = data.items.map { it.toRepository() }))
                } else {
                    emit(Resource.Error(message = "Search result is null"))
                }
            } else {
                emit(Resource.Error(message = result.message()))
            }
        }
    }

    override suspend fun searchRepositories(
        q: String,
        page: Int,
        pageSize: Int
    ): Result<List<Repository>> {
        val data = api.searchRepositoriesRes(q, page, pageSize)
        if (data.isSuccessful) {
            return Result.success(data.body()?.items?.map { it.toRepository() } ?: emptyList())
        } else {
            return Result.failure(Exception("error"))
        }
    }
}