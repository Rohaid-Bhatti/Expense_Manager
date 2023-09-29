package com.hzm.expensemanager.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val email : String,
    val password : String,
    val number : String,
)
