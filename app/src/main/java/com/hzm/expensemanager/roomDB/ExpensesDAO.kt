package com.hzm.expensemanager.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpensesDAO {

    @Query("SELECT * FROM expense_table")
    fun getAllData() : LiveData<List<ExpensesEntity>>

    @Insert
    suspend fun insertData(expense : ExpensesEntity)

    @Query("SELECT * FROM expense_table WHERE status = :status")
    fun getExpenseItemsByStatus(status: String): LiveData<List<ExpensesEntity>>

    @Query("SELECT * FROM expense_table WHERE status = 'Expense' AND createdDate BETWEEN :startDate AND :endDate")
    fun getExpensesBetweenDates(startDate: Long, endDate: Long): LiveData<List<ExpensesEntity>>

    @Query("SELECT * FROM expense_table WHERE status = 'Income' AND createdDate BETWEEN :startDate AND :endDate")
    fun getIncomeBetweenDates(startDate: Long, endDate: Long): LiveData<List<ExpensesEntity>>
}