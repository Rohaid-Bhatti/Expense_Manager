package com.hzm.expensemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.roomDB.ExpensesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ExpenseViewModel(private val expenseRepository : ExpenseRepository) : ViewModel() {

    fun getData() : LiveData<List<ExpensesEntity>> {
        return expenseRepository.getAllData()
    }

    fun insertData(expense : ExpensesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertData(expense)
        }
    }

    fun getExpenseItemsByStatus(status: String): LiveData<List<ExpensesEntity>> {
        return expenseRepository.getExpenseItemsByStatus(status)
    }

    fun getExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<List<ExpensesEntity>> {
        return expenseRepository.getExpensesBetweenDates(startDate, endDate)
    }

    fun getIncomeBetweenDates(startDate: Date, endDate: Date): LiveData<List<ExpensesEntity>> {
        return expenseRepository.getIncomeBetweenDates(startDate, endDate)
    }
}