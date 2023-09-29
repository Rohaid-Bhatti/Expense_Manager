package com.hzm.expensemanager.respository

import androidx.lifecycle.LiveData
import com.hzm.expensemanager.roomDB.UserDAO
import com.hzm.expensemanager.roomDB.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao : UserDAO) {

    fun getAllData() : LiveData<List<UserEntity>> {
        return userDao.getAllData()
    }

    suspend fun insertData(expense : UserEntity) {
        userDao.insertData(expense)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): LiveData<UserEntity?> {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmailAndPassword(email, password)
        }
    }
}