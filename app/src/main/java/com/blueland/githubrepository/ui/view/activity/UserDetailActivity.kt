package com.blueland.githubrepository.ui.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.ViewModelProvider
import com.blueland.githubrepository.R
import com.blueland.githubrepository.ui.base.BaseActivity
import com.blueland.githubrepository.databinding.ActivityUserDetailBinding
import com.blueland.githubrepository.data.repository.Repository
import com.blueland.githubrepository.ui.viewmodel.UserDetailViewModel
import com.blueland.githubrepository.widget.extension.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget

/**
 * 유저 상세 정보 화면
 */
class UserDetailActivity : BaseActivity<ActivityUserDetailBinding>(R.layout.activity_user_detail) {

    private val viewModelFactory: UserDetailViewModel.Factory by lazy { UserDetailViewModel.Factory(Repository()) }
    private val viewModel: UserDetailViewModel by lazy { ViewModelProvider(this, viewModelFactory)[UserDetailViewModel::class.java] }

    private lateinit var login: String

    companion object {
        fun start(context: Context, login: String) {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("login", login)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        login = intent.getStringExtra("login")!!
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.userDetail.observe(this) { userDetail ->
            loadImage(binding.ivProfile, userDetail.avatarURL)
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

            btnRepositories.setOnClickListener {
                // 유저 레파지토리 화면으로 이동
                UserRepositoryActivity.start(this@UserDetailActivity, login)
            }
        }
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
        viewModel.requestUserDetail(login)
    }

    private fun loadImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(imageUrl)
            .centerCrop().into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.resources, resource)
                    circularBitmapDrawable.isCircular = true
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }
}