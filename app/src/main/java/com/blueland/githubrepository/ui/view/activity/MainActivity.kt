package com.blueland.githubrepository.ui.view.activity

import android.util.Log
import android.view.KeyEvent
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.R
import com.blueland.githubrepository.ui.base.BaseActivity
import com.blueland.githubrepository.databinding.ActivityMainBinding
import com.blueland.githubrepository.data.model.UserEntity
import com.blueland.githubrepository.data.repository.Repository
import com.blueland.githubrepository.ui.view.adapter.UserAdapter
import com.blueland.githubrepository.ui.viewmodel.MainViewModel
import com.blueland.githubrepository.widget.extension.hideSoftKeyboard
import com.blueland.githubrepository.widget.extension.toast

/**
 * 유저 검색 화면
 */
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), UserAdapter.ItemOnClickListener {

    private val viewModelFactory: MainViewModel.Factory by lazy { MainViewModel.Factory(Repository()) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java] }

    private lateinit var adapter: UserAdapter

    override fun initView() {
        super.initView()
        initRecyclerView()
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.userList.observe(this) {
            Log.e(TAG, "initObserver: userList.observe it.size=${it.size}")
            // 유저 검색 목록 업데이트
            adapter.setUserList(it)
        }
        viewModel.showToastEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { text ->
                toast(text)
            }
        }
    }

    private var lastTimeBackPressed: Long = 0
    override fun initListener() {
        super.initListener()
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - lastTimeBackPressed >= 1500) {
                    lastTimeBackPressed = System.currentTimeMillis()
                    toast(R.string.toast_back_pressed)
                } else {
                    finish()
                }
            }
        })

        // 유저 검색 EditText 엔터 버튼 클릭 처리
        binding.let {
            it.etKeyword.setOnKeyListener { _, keyCode, _ ->
                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        viewModel.requestUserSearch()
                        hideSoftKeyboard(this@MainActivity)
                    }
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun initRecyclerView() {
        // RecyclerView 초기화
        adapter = UserAdapter(this)
        binding.recyclerView.let {
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount
                    if (lastVisibleItemPosition + 1 == itemTotalCount) {
                        viewModel.requestPagingUserSearch()
                    }
                }
            })
        }
    }

    override fun itemOnClick(pos: Int, item: UserEntity) {
        // 유저 상세 정보 화면으로 이동
        UserDetailActivity.start(this, item.login)
    }

    override fun onResume() {
        super.onResume()
    }
}