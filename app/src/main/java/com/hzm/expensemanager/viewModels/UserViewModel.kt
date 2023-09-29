package com.hzm.expensemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzm.expensemanager.respository.UserRepository
import com.hzm.expensemanager.roomDB.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getData() : LiveData<List<UserEntity>> {
        return userRepository.getAllData()
    }

    fun insertData(user : UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertData(user)
        }
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): LiveData<UserEntity?> {
        return userRepository.getUserByEmailAndPassword(email, password)
    }
}