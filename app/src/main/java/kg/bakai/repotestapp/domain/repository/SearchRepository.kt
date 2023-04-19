package kg.bakai.repotestapp.domain.repository

import kg.bakai.repotestapp.domain.model.Repository
import kg.bakai.repotestapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchRepositories(q: String): Flow<Resource<List<Repository>>>
    suspend fun searchRepositories(q: String, page: Int, pageSize: Int): Result<List<Repository>>
}