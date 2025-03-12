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

class logInActivity : AppCompatActivity() {
    private lateinit var userRepo: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        val etUsername : TextView = findViewById(R.id.inputName)
        val etPassword : TextView = findViewById(R.id.inputPassword)
        val autoUsername = intent.getStringExtra("AUTO_FILL_USERNAME")
        etUsername.setText(autoUsername)
        //跳转注册页面
        val register: TextView = findViewById(R.id.registerJump)
        register.setOnClickListener {
            val intent = Intent(this, registerActivity::class.java)
            startActivity(intent)
        }
        //点击登录
        val login: Button = findViewById(R.id.logIn)
        userRepo = UserRepository(this)

        login.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val user = userRepo.getUser(username)
            if (user != null && SecurityUtils.verifyPassword(password, user.hashedPassword)) {
                // 登录成功
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            }
        }
    }
}