package com.syq.meituan.Repository

// UserRepository.kt
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.syq.meituan.User

class UserRepository(context: Context) {
    //通过 lazy 关键字实现延迟初始化，只有在第一次访问 sharedPreferences 时才会执行代码块内的逻辑。
    private val sharedPreferences by lazy {
        //生成一个用于加密数据的密钥。 MasterKey 是 Android Jetpack Security 库提供的密钥管理工具。
        //AES256_GCM 指定加密算法为 AES-256（密钥长度 256 位），模式为 GCM。
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "user_password", //存储文件名
            masterKey,                //加密用的主密钥
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, //键加密方案
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM //值加密方案
        )
    }

    // 保存用户
    fun saveUser(user: User) {
        if (sharedPreferences.contains(user.username)) {
            throw Exception("用户名已存在")
        }
        sharedPreferences.edit()
            .putString(user.username, user.hashedPassword)
            .apply()
    }

    // 获取用户
    fun getUser(username: String): User? {
        val hashedPwd = sharedPreferences.getString(username, null)
        return hashedPwd?.let { User(username, it) }
    }
}