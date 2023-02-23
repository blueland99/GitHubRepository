package com.blueland.githubrepository.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blueland.githubrepository.R
import com.blueland.githubrepository.global.App
import com.blueland.githubrepository.widget.extension.hideSoftKeyboard
import com.blueland.githubrepository.widget.util.NetworkConnection

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {

    protected val TAG = javaClass.simpleName

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()

        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // 초기화된 layoutResId로 DataBinding 객체 생성
        binding = DataBindingUtil.setContentView(this, layoutResId)

        // 네트워크 상태 체크
        val connection = NetworkConnection(applicationContext)
        connection.observe(this) { isConnected ->
            Log.e(TAG, "NetworkConnection -> isConnected $isConnected")
            if (isConnected) {

            } else {
                App.getInstance().showAlertDialog(this, getString(R.string.alert_network)) { _, _ ->
                    finishAffinity()
                }
                return@observe
            }
        }

        initView()
        initViewModel()
        initObserver()
        initListener()
        afterOnCreate()
    }

    protected open fun beforeSetContentView() {}
    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initObserver() {}
    protected open fun initListener() {}
    protected open fun afterOnCreate() {}

    // 포커스 영역 외 터치 시 키보드 내림
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            val focusView = currentFocus
            if (focusView != null) {
                val rect = Rect()
                focusView.getGlobalVisibleRect(rect)
                val x = it.x.toInt()
                val y = it.y.toInt()
                if (!rect.contains(x, y)) {
                    hideSoftKeyboard(this)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}