package kg.bakai.repotestapp.domain.model

import kg.bakai.repotestapp.data.remote.dto.OwnerDto

data class Repository(
    val name: String?,
    val language: String?,
    val owner: Owner?,
    val url: String?
)
