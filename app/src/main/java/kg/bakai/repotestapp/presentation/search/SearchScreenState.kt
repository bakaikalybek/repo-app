package kg.bakai.repotestapp.presentation.search

import kg.bakai.repotestapp.domain.model.Repository

data class SearchScreenState(
    val repositories: List<Repository> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val page: Int = 0
)