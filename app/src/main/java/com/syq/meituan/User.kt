package com.syq.meituan

// User.kt
data class User(
    val username: String,
    val hashedPassword: String, // 存储哈希后的密码
    val salt: String? = null // BCrypt 不需要单独存盐
)
