package kg.bakai.repotestapp.presentation.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.bakai.repotestapp.domain.repository.SearchRepository
import kg.bakai.repotestapp.util.DefaultPaginator
import kg.bakai.repotestapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): ViewModel() {

    var state by mutableStateOf(SearchScreenState())

    private var searchJob: Job? = null

    fun onEvent(event: SearchScreenEvents) {
        when (event) {
            is SearchScreenEvents.OnSearchQuery -> {
                state = state.copy(
                    searchQuery = event.query
                )
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(300)
                    getRepositories(query = state.searchQuery)
                }
            }
        }
    }

    private val paginator = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.searchRepositories(q = state.searchQuery, page = nextPage, pageSize = 20)
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            Log.i("ViewModel", "${it?.localizedMessage}")
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            Log.i("ViewModel", "${items.size}")
            state = state.copy(
                repositories = state.repositories + items,
                page = newKey
            )
        }
    )

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNext()
        }
    }

    private fun getRepositories(
        query: String = state.searchQuery
    ) {
        viewModelScope.launch {
            repository.searchRepositories(q = query)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { newData ->
                                state = state.copy(
                                    repositories = newData,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                        else -> {
                            state = state.copy(
                                isLoading = true
                            )
                        }
                    }
                }
        }
    }
}