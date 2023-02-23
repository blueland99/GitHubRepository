package com.blueland.githubrepository.ui.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.blueland.githubrepository.ui.base.BaseViewModel
import com.blueland.githubrepository.data.model.UserDetailEntity
import com.blueland.githubrepository.data.repository.Repository
import kotlinx.coroutines.launch

class UserDetailViewModel(private val repository: Repository) : BaseViewModel() {

    val progressBarVisibility: ObservableInt = ObservableInt(View.GONE)

    private val _userDetail = MutableLiveData<UserDetailEntity>()
    val userDetail: LiveData<UserDetailEntity> get() = _userDetail

    val avatarURL: ObservableField<String> = ObservableField<String>()
    val name: ObservableField<String> = ObservableField<String>()
    val login: ObservableField<String> = ObservableField<String>()

    val followers: ObservableField<Long> = ObservableField<Long>()
    val following: ObservableField<Long> = ObservableField<Long>()
    val followersVisibility: ObservableInt = ObservableInt(View.GONE)

    val bio: ObservableField<String> = ObservableField<String>()
    val bioVisibility: ObservableInt = ObservableInt(View.GONE)

    val blog: ObservableField<String> = ObservableField<String>()
    val blogVisibility: ObservableInt = ObservableInt(View.GONE)

    val email: ObservableField<String> = ObservableField<String>()
    val emailVisibility: ObservableInt = ObservableInt(View.GONE)

    val location: ObservableField<String> = ObservableField<String>()
    val locationVisibility: ObservableInt = ObservableInt(View.GONE)

    val twitter: ObservableField<String> = ObservableField<String>()
    val twitterVisibility: ObservableInt = ObservableInt(View.GONE)

    val company: ObservableField<String> = ObservableField<String>()
    val companyVisibility: ObservableInt = ObservableInt(View.GONE)

    val publicRepos: ObservableField<Long> = ObservableField<Long>(0)
    val publicReposVisibility: ObservableInt = ObservableInt(View.GONE)

    private val _showToastEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>> = _showToastEvent

    fun requestUserDetail(query: String) {
        // 로딩중 진행바 표시
        progressBarVisibility.set(View.VISIBLE)

        // 유저 상세 정보 조회 API 실행
        viewModelScope.launch {
            val response = repository.getUserDetail(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    _userDetail.value = it
                    avatarURL.set(it.avatarURL)
                    name.set(it.name)
                    login.set(it.login)
                    bio.set(it.bio)
                    blog.set(it.blog)
                    email.set(it.email)
                    location.set(it.location)
                    twitter.set(it.twitterUsername)
                    company.set(it.company)
                    followers.set(it.followers)
                    following.set(it.following)
                    publicRepos.set(it.publicRepos)
                    bioVisibility.set(if (bio.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    blogVisibility.set(if (blog.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    emailVisibility.set(if (email.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    locationVisibility.set(if (location.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    twitterVisibility.set(if (twitter.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    companyVisibility.set(if (company.get().isNullOrBlank()) View.GONE else View.VISIBLE)
                    followersVisibility.set(if (followers.get() == null) View.GONE else View.VISIBLE)
                    publicReposVisibility.set(if (publicRepos.get() == null) View.GONE else View.VISIBLE)
                } ?: run {
                    showToast("오류가 발생했습니다.")
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
            if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
                return UserDetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}