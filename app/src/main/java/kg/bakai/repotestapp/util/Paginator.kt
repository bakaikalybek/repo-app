package kg.bakai.repotestapp.util

interface Paginator<Key, Item> {
    suspend fun loadNext()
    fun reset()
}