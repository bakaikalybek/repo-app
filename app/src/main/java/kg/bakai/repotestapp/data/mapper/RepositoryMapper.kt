package kg.bakai.repotestapp.data.mapper

import kg.bakai.repotestapp.data.remote.dto.OwnerDto
import kg.bakai.repotestapp.data.remote.dto.RepositoryDto
import kg.bakai.repotestapp.domain.model.Owner
import kg.bakai.repotestapp.domain.model.Repository

fun RepositoryDto.toRepository(): Repository {
    return Repository(
        name = name,
        language = language,
        owner = owner.toOwner(),
        url = url
    )
}

fun OwnerDto.toOwner(): Owner {
    return Owner(
        login = login,
        avatar = avatar
    )
}