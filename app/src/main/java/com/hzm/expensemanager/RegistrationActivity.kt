package com.hzm.expensemanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hzm.expensemanager.databinding.ActivityMainBinding
import com.hzm.expensemanager.databinding.ActivityRegistrationBinding
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.respository.UserRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.roomDB.UserEntity
import com.hzm.expensemanager.utils.ValidationUtils
import com.hzm.expensemanager.viewModels.ExpenseViewModel
import com.hzm.expensemanager.viewModels.ExpenseViewModelFactory
import com.hzm.expensemanager.viewModels.UserViewModel
import com.hzm.expensemanager.viewModels.UserViewModelFactory

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isCheck: Boolean

        val dao = ExpensesDatabase.getDatabase(applicationContext).userDao()
        val repository = UserRepository(dao)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(repository)
        ).get(UserViewModel::class.java)

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val number = binding.editTextPhone.text.toString()

            // for validations
            // Validate input fields
            if (name.isEmpty() || email.isEmpty() || number.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidEmail(email)) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate phone number: starts with "03" and is 11 digits long
            if (!ValidationUtils.isValidPhoneNumber(number)) {
                Toast.makeText(
                    this,
                    "Invalid phone number. Please enter a valid 11-digit number starting with \\'03\\'",
                    Toast.LENGTH_SHORT
                ).show()
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

            isCheck = true
            val sharedPreference = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("USER_EMAIL", email)
            editor.putBoolean("ISCHECK", isCheck)
            editor.apply()

            val user =  UserEntity(0, name, email, password, number)
            userViewModel.insertData(user)

            // Navigate to the DashboardActivity or the next screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(
                this,
                "Account created successfully",
                Toast.LENGTH_SHORT
            ).show()

            // Finish the current activity (MainActivity)
            finish()
        }

        binding.tvSignin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}