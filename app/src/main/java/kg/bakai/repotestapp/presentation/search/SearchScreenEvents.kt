package kg.bakai.repotestapp.presentation.search

sealed class SearchScreenEvents {
    data class OnSearchQuery(val query: String): SearchScreenEvents()
}