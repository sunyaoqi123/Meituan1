package com.syq.meituan.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syq.meituan.R
import com.syq.meituan.Repository.UserRepository
import com.syq.meituan.SecurityUtils
import com.syq.meituan.User

class registerActivity : AppCompatActivity() {
    private lateinit var userRepo: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val register: Button = findViewById(R.id.register)
        val back: Button = findViewById(R.id.backBtn)
        back.setOnClickListener {
            val intent = Intent(this, logInActivity::class.java)
            startActivity(intent)
            finish()
        }
        userRepo = UserRepository(this)
        register.setOnClickListener {
            val etUsername: TextView = findViewById(R.id.etUsername)
            val etPassword: TextView = findViewById(R.id.etPassword)
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(username, password)) {
                try {
                    // 哈希密码
                    val hashedPassword = SecurityUtils.hashPassword(password)

                    // 保存用户
                    userRepo.saveUser(User(username, hashedPassword))
                    Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, logInActivity::class.java).apply {
                        putExtra("AUTO_FILL_USERNAME", username)
                    }
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this, "注册失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun validateInput(username: String, password: String): Boolean {
        // 添加输入验证（例如长度、复杂度等）
        if(username.isNotBlank() && password.length >= 8){
            return true
        }else{
            Toast.makeText(this, "用户名不能为空，密码长度应大于8", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}