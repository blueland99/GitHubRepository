package com.blueland.githubrepository.data.network

import com.blueland.githubrepository.data.model.UserDetailEntity
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.data.model.response.SearchUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit 으로 GitHub API 를 이용하기 위한 클래스
 */
interface ApiService {
    /**
     * GitHub 유저 검색 API
     * @param query 검색할 유저 키워드
     * @param page 가져올 결과의 페이지 번호
     * @param limit 페이지당 결과 수 (최대 100개)
     * @return 검색된 유저 목록
     */
    @GET("search/users")
    suspend fun getSearchUser(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") limit: Int? = 30
    ): Response<SearchUserResponse>

    /**
     * GitHub 유저 레파지토리 조회 API
     * @param login 유저 아이디
     * @param page 가져올 결과의 페이지 번호
     * @param limit 페이지당 결과 수 (최대 100개)
     * @return 레파지토리 목록
     */
    @GET("users/{login}/repos")
    suspend fun getUserRepository(
        @Path("login") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") limit: Int? = 30
    ): Response<List<UserRepositoryEntity>>

    /**
     * GitHub 유저 상세 정보 조회 API
     * @param login 유저 아이디
     * @return
     */
    @GET("users/{login}")
    suspend fun getUserDetail(
        @Path("login") query: String,
    ): Response<UserDetailEntity>
}