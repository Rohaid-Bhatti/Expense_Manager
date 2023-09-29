package com.hzm.expensemanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hzm.expensemanager.databinding.ActivityLoginBinding
import com.hzm.expensemanager.respository.UserRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.utils.ValidationUtils
import com.hzm.expensemanager.viewModels.UserViewModel
import com.hzm.expensemanager.viewModels.UserViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = ExpensesDatabase.getDatabase(applicationContext).userDao()
        val userRepository = UserRepository(dao)
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidEmail(email)) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6 || !ValidationUtils.isValidPassword(password)) {
                Toast.makeText(
                    this,
                    "Password must be at least 6 characters long and contain 1 number and 1 special character",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Perform login using a coroutine
            lifecycleScope.launch {
                val user = userViewModel.getUserByEmailAndPassword(email, password)
                if (user != null) {
                    // Login successful
                    val sharedPreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("USER_EMAIL", email)
                    editor.putBoolean("ISCHECK", true)
                    editor.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login failed
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSignUp.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}