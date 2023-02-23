package com.blueland.githubrepository.data.repository

import com.blueland.githubrepository.data.model.UserDetailEntity
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.data.network.RetrofitInstance
import com.blueland.githubrepository.data.model.response.SearchUserResponse
import retrofit2.Response

class Repository {

    suspend fun getSearchUser(
        query: String,
        page: Int = 1,
        limit: Int? = 30
    ): Response<SearchUserResponse> {
        return RetrofitInstance.api.getSearchUser(query, page, limit)
    }

    suspend fun getUserRepository(
        query: String,
        page: Int = 1,
        limit: Int? = 30
    ): Response<List<UserRepositoryEntity>> {
        return RetrofitInstance.api.getUserRepository(query, page, limit)
    }

    suspend fun getUserDetail(
        query: String
    ): Response<UserDetailEntity> {
        return RetrofitInstance.api.getUserDetail(query)
    }
}