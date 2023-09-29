package com.hzm.expensemanager.respository

import androidx.lifecycle.LiveData
import com.hzm.expensemanager.roomDB.ExpensesDAO
import com.hzm.expensemanager.roomDB.ExpensesEntity
import java.util.*

class ExpenseRepository(private val expenseDao : ExpensesDAO) {

    fun getAllData() : LiveData<List<ExpensesEntity>> {
        return expenseDao.getAllData()
    }

    suspend fun insertData(expense : ExpensesEntity) {
        expenseDao.insertData(expense)
    }

    fun getExpenseItemsByStatus(status: String): LiveData<List<ExpensesEntity>> {
        return expenseDao.getExpenseItemsByStatus(status)
    }

    fun getExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<List<ExpensesEntity>> {
        val startDateLong = startDate.time
        val endDateLong = endDate.time
        return expenseDao.getExpensesBetweenDates(startDateLong, endDateLong)
    }

    fun getIncomeBetweenDates(startDate: Date, endDate: Date): LiveData<List<ExpensesEntity>> {
        val startDateLong = startDate.time
        val endDateLong = endDate.time
        return expenseDao.getIncomeBetweenDates(startDateLong, endDateLong)
    }
}