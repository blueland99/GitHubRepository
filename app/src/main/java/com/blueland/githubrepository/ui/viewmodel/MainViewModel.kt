package com.blueland.githubrepository.ui.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.blueland.githubrepository.data.model.UserEntity
import com.blueland.githubrepository.data.repository.Repository
import com.blueland.githubrepository.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : BaseViewModel() {

    val progressBarVisibility: ObservableInt = ObservableInt(View.GONE)
    val noneTextVisibility: ObservableInt = ObservableInt(View.VISIBLE)
    val deleteButtonVisibility: ObservableInt = ObservableInt(View.INVISIBLE)

    private var _query: String = ""
    val query = ObservableField<String>()

    private var _page: Int = 1

    private val _userList = MutableLiveData<ArrayList<UserEntity>>()
    val userList: LiveData<ArrayList<UserEntity>> get() = _userList

    private val _showToastEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>> = _showToastEvent

    fun requestUserSearch() {
        Log.d(TAG, "requestUserSearch: ")
        _query = query.get()?.trim() ?: ""
        if (_query.isEmpty()) {
            showToast("검색어를 입력해주세요.")
            return
        }

        // 로딩중 진행바 표시
        progressBarVisibility.set(View.VISIBLE)

        // 유저 검색 API 실행
        _page = 1
        viewModelScope.launch {
            val response = repository.getSearchUser(query = _query)
            if (response.isSuccessful) {
                response.body()?.apply {
                    if (items.isEmpty()) {
                        _userList.value = arrayListOf()
                        noneTextVisibility.set(View.VISIBLE)
                    } else {
                        _userList.value = items as ArrayList<UserEntity>
                        noneTextVisibility.set(View.GONE)
                    }
                }
            } else {
                showToast("오류가 발생했습니다.")
            }
            progressBarVisibility.set(View.GONE)
        }
    }

    fun requestPagingUserSearch() {
        _page++
        Log.d(TAG, "requestPagingUserSearch: page=$_page")
        progressBarVisibility.set(View.VISIBLE)

        // 유저 검색 API 실행, 페이징 처리를 위한 page 지정 후 호출
        viewModelScope.launch {
            val response = repository.getSearchUser(query = _query, page = _page)
            if (response.isSuccessful) {
                response.body()?.apply {
                    if (items.isEmpty()) {
                        // 마지막 페이지
                        _page--
                    } else {
                        val newList = _userList.value
                        newList?.addAll(items)
                        _userList.value = newList
                    }
                }
            } else {
                showToast("오류가 발생했습니다.")
            }
            progressBarVisibility.set(View.GONE)
        }
    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val str = s.toString()
            deleteButtonVisibility.set(if (str.isEmpty()) View.INVISIBLE else View.VISIBLE)
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    fun onClickDelete() {
        query.set("")
    }

    fun showToast(message: String) {
        _showToastEvent.value = Event(message)
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}