package com.example.sub1intermediate.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sub1intermediate.ui.main.MainActivity
import com.example.sub1intermediate.R
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.data.repository.Result
import com.example.sub1intermediate.databinding.ActivityLoginBinding
import com.example.sub1intermediate.ui.auth.register.Register

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_noactionbar)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels {
            factory
        }
        loginViewModel.apply {
            getToken().observe(this@Login) {
                if (it != null) {
                    toHome()
                }
            }
        }
        binding.apply {
            loginBtn.setOnClickListener {
                val email = binding.loginEmail.text.toString().trim()
                val password = binding.loginPassword.text.toString().trim()
                loginViewModel.getUserLogin(email, password).observe(this@Login) {
                    when (it) {
                        is Result.Error -> {
                            Toast.makeText(this@Login, "FAILED", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this@Login, "LOGIN SUCCESSFULL", Toast.LENGTH_SHORT).show()
                            toHome()

                        }
                    }
                }
            }

            registerBtn.setOnClickListener {
                val intent = Intent(this@Login, Register::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun toHome() {
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}