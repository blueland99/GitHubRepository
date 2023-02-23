package com.blueland.githubrepository.data.model.response

import com.blueland.githubrepository.data.model.UserEntity
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("total_count")
    val totalCount: Long,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<UserEntity>
)