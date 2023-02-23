package com.blueland.githubrepository.widget.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 유틸성 확장 함수를 정의
 */

// show toast message by String resource
internal fun Context.toast(@StringRes res: Int) {
    Toast.makeText(this, getString(res), Toast.LENGTH_SHORT).show()
}

// show toast message by String
internal fun Context.toast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}