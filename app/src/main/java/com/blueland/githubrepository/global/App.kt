package com.blueland.githubrepository.global

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.blueland.githubrepository.R

class App : Application() {

    private val TAG = javaClass.simpleName
    private var builder: AlertDialog? = null

    companion object {
        private lateinit var application: App
        fun getInstance(): App = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    /**
     * AlertDialog
     * @param context
     * @param message 메세지
     * @param onClickListener '확인' 버튼 이벤트 리스너
     */
    fun showAlertDialog(context: Context, message: String?, onClickListener: DialogInterface.OnClickListener?) {
        if (builder != null) {
            builder?.dismiss()
            builder = null
        }
        builder = AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(R.string.alert_confirm, onClickListener)
            .setCancelable(false)
            .create()
        builder?.show()
    }
}