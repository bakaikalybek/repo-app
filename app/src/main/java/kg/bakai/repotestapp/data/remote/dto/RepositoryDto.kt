package kg.bakai.repotestapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDto(
    @SerializedName("full_name") val name: String,
    @SerializedName("language") val language: String,
    @SerializedName("owner") val owner: OwnerDto,
    @SerializedName("html_url") val url: String
)