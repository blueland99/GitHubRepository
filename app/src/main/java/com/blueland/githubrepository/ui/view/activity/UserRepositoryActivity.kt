package com.blueland.githubrepository.ui.view.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.R
import com.blueland.githubrepository.ui.base.BaseActivity
import com.blueland.githubrepository.databinding.ActivityUserRepositoryBinding
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.data.repository.Repository
import com.blueland.githubrepository.ui.view.adapter.UserRepositoryAdapter
import com.blueland.githubrepository.ui.viewmodel.UserRepositoryViewModel
import com.blueland.githubrepository.widget.extension.toast

/**
 * 유저 레파지토리 화면
 */
class UserRepositoryActivity : BaseActivity<ActivityUserRepositoryBinding>(R.layout.activity_user_repository), UserRepositoryAdapter.ItemOnClickListener {

    private val viewModelFactory: UserRepositoryViewModel.Factory by lazy { UserRepositoryViewModel.Factory(Repository()) }
    private val viewModel: UserRepositoryViewModel by lazy { ViewModelProvider(this, viewModelFactory)[UserRepositoryViewModel::class.java] }

    private lateinit var adapter: UserRepositoryAdapter

    private lateinit var login: String

    companion object {
        fun start(context: Context, login: String) {
            val intent = Intent(context, UserRepositoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("login", login)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        login = intent.getStringExtra("login")!!
        initRecyclerView()
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.repositoryList.observe(this) {
            Log.e(TAG, "initObserver: repositories.observe it.size=${it.size}")
            // 유저 레파지토리 목록 업데이트
            adapter.setRepositoryList(it)
        }
        viewModel.showToastEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { text ->
                toast(text)
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            topLayout.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun initRecyclerView() {
        // RecyclerView 초기화
        adapter = UserRepositoryAdapter(this)
        binding.recyclerView.let {
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount
                    if (lastVisibleItemPosition + 1 == itemTotalCount) {
                        viewModel.requestPagingUserRepository()
                    }
                }
            })
        }
    }

    override fun itemOnClick(pos: Int, item: UserRepositoryEntity) {

    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUserRepository(login)
    }
}