package com.hzm.expensemanager.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_table")
    fun getAllData() : LiveData<List<UserEntity>>

    @Insert
    suspend fun insertData(user : UserEntity)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): LiveData<UserEntity?>
}