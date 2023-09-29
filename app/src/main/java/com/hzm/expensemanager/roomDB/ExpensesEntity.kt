package com.hzm.expensemanager.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "expense_table")
@TypeConverters(Converters::class)
data class ExpensesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: String,
    val status: String,
    val reason: String,
    val description: String,
    val createdDate: Date,
    val documentName: String,
    val userEmail: String
)
