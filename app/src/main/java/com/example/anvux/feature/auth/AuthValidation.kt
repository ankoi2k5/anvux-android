package com.example.anvux.feature.auth

private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

fun validateName(name: String): String? = when {
    name.isBlank() -> "Vui lòng nhập họ tên"
    else -> null
}

fun validateEmail(email: String): String? = when {
    email.isBlank() -> "Vui lòng nhập email"
    !EMAIL_REGEX.matches(email.trim()) -> "Email không hợp lệ"
    else -> null
}

fun validatePassword(password: String): String? = when {
    password.isBlank() -> "Vui lòng nhập mật khẩu"
    password.length < 8 -> "Mật khẩu tối thiểu 8 ký tự"
    else -> null
}

fun validateConfirmPassword(password: String, confirm: String): String? = when {
    confirm != password -> "Mật khẩu nhập lại không khớp"
    else -> null
}