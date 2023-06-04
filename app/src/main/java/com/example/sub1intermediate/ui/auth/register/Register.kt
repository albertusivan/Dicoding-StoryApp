package com.example.sub1intermediate.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sub1intermediate.R
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.data.repository.Result
import com.example.sub1intermediate.databinding.ActivityRegisterBinding
import com.example.sub1intermediate.ui.auth.login.Login

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_noactionbar)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val registerViewModel: RegisterViewModel by viewModels {
            factory
        }
        setContentView(binding.root)
        binding.apply {
            registerBtn.setOnClickListener {
                val name = binding.signupName.text.toString().trim()
                val email = binding.signupEmail.text.toString().trim()
                val password = binding.signupPassword.text.toString().trim()

                registerViewModel.saveUserRegister(name, email, password).observe(this@Register) {
                    when (it) {
                        is Result.Error -> {
                            Toast.makeText(this@Register, "FAILED", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this@Register, "REGISTER SUCCESSFULL", Toast.LENGTH_SHORT).show()
                            toLogin()
                        }
                    }
                }
            }
            loginBtn.setOnClickListener {
                toLogin()
            }
        }
    }

    private fun toLogin() {
        val intent = Intent(this@Register, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

}