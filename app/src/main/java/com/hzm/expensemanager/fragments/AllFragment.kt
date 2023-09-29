package com.hzm.expensemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzm.expensemanager.R
import com.hzm.expensemanager.adapters.AllAdapter
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.roomDB.ExpensesEntity
import com.hzm.expensemanager.viewModels.ExpenseViewModel
import com.hzm.expensemanager.viewModels.ExpenseViewModelFactory
import java.util.*

class AllFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var textExpenses: TextView
    private lateinit var textIncome: TextView
    private lateinit var textProfitLoss: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        adapter = AllAdapter()
        recyclerView.adapter = adapter

        textExpenses = view.findViewById(R.id.textExpenses)
        textIncome = view.findViewById(R.id.textIncome)
        textProfitLoss = view.findViewById(R.id.textProfitLoss)

        val dao = ExpensesDatabase.getDatabase(requireContext()).expensesDao()
        val repository = ExpenseRepository(dao)

        // Initialize the ViewModel
//        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        expenseViewModel = ViewModelProvider(this, ExpenseViewModelFactory(repository))
            .get(ExpenseViewModel::class.java)

        // Calculate the start date (30 days ago from the current date)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -30)
        val startDate = calendar.time

        // Get the current date
        val endDate = Calendar.getInstance().time

        // Observe the LiveData from the ViewModel and update the adapter when it changes
        expenseViewModel.getExpensesBetweenDates(startDate, endDate)
            .observe(viewLifecycleOwner, Observer { expenses ->
                adapter.setExpenses(expenses) // Update the adapter with the new data
            })

        // Observe the LiveData for income between dates
        expenseViewModel.getIncomeBetweenDates(startDate, endDate)
            .observe(viewLifecycleOwner, Observer { income ->
                // Handle income data here
            })

        // Observe the LiveData from the ViewModel and update the adapter when it changes
        expenseViewModel.getData().observe(viewLifecycleOwner, Observer { expenses ->
            adapter.setExpenses(expenses) // Update the adapter with the new data

            // Calculate expenses and income totals
            val expensesTotal = expenses.filter { it.status == "Expense" }.sumByDouble { it.amount.toDouble() }
            val incomeTotal = expenses.filter { it.status == "Income" }.sumByDouble { it.amount.toDouble() }
            val profitLoss = incomeTotal - expensesTotal

            textExpenses.text = String.format("%.2f", expensesTotal)
            textIncome.text = String.format("%.2f", incomeTotal)
            textProfitLoss.text = String.format("Profit/Loss: %.2f", profitLoss)
        })
        return view
    }
}