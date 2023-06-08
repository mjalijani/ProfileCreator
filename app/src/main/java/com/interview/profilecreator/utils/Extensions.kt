package com.interview.profilecreator.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import java.util.Base64

fun String.encodeToBae64(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

fun String.decodeFromBase64(): String {
    return String(Base64.getDecoder().decode(this))
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isUrlValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.WEB_URL.matcher(this).matches()
}

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    )
        .show()
}