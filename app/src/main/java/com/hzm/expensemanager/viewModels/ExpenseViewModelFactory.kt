package com.hzm.expensemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hzm.expensemanager.respository.ExpenseRepository

class ExpenseViewModelFactory(private val expenseRepository: ExpenseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(expenseRepository) as T
    }
}