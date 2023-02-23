package com.blueland.githubrepository.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.blueland.githubrepository.ui.base.BaseViewModel
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.data.repository.Repository
import kotlinx.coroutines.launch

class UserRepositoryViewModel(private val repository: Repository) : BaseViewModel() {

    val progressBarVisibility: ObservableInt = ObservableInt(View.GONE)
    val noneTextVisibility: ObservableInt = ObservableInt(View.VISIBLE)

    private var _login: String = ""

    private var _page: Int = 1

    private val _repositoryList = MutableLiveData<ArrayList<UserRepositoryEntity>>()
    val repositoryList: LiveData<ArrayList<UserRepositoryEntity>> get() = _repositoryList

    private val _showToastEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>> = _showToastEvent

    fun requestUserRepository(mLogin: String) {
        Log.d(TAG, "requestUserRepository: ")
        // 로딩중 진행바 표시
        progressBarVisibility.set(View.VISIBLE)

        // 유저 레포지토리 조회 API 실행
        _page = 1
        _login = mLogin
        viewModelScope.launch {
            val response = repository.getUserRepository(query = _login)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isEmpty()) {
                        _repositoryList.value = arrayListOf()
                        noneTextVisibility.set(View.VISIBLE)
                    } else {
                        _repositoryList.value = it as ArrayList<UserRepositoryEntity>
                        noneTextVisibility.set(View.GONE)
                    }
                }
            } else {
                showToast("오류가 발생했습니다.")
            }
            progressBarVisibility.set(View.GONE)
        }
    }

    fun requestPagingUserRepository() {
        _page++
        Log.d(TAG, "requestPagingUserRepository: page=$_page")
        progressBarVisibility.set(View.VISIBLE)

        // 유저 레포지토리 조회 API 실행, 페이징 처리를 위한 page 지정 후 호출
        viewModelScope.launch {
            val response = repository.getUserRepository(query = _login, page = _page)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isEmpty()) {
                        // 마지막 페이지
                        _page--
                    } else {
                        val newList = _repositoryList.value
                        newList?.addAll(it)
                        _repositoryList.value = newList
                    }
                }
            } else {
                showToast("오류가 발생했습니다.")
            }
            progressBarVisibility.set(View.GONE)
        }
    }

    fun showToast(message: String) {
        _showToastEvent.value = Event(message)
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserRepositoryViewModel::class.java)) {
                return UserRepositoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}