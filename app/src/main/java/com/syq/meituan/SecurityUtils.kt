package com.syq.meituan

import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityUtils {
    // 生成BCrypt哈希密码
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    // 验证密码
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}