package com.hzm.expensemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzm.expensemanager.R
import com.hzm.expensemanager.adapters.ExpenseAdapter
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.viewModels.ExpenseViewModel
import com.hzm.expensemanager.viewModels.ExpenseViewModelFactory

class ExpenseFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ExpenseAdapter()
        recyclerView.adapter = adapter

        val dao = ExpensesDatabase.getDatabase(requireContext()).expensesDao()
        val repository = ExpenseRepository(dao)

        expenseViewModel = ViewModelProvider(this, ExpenseViewModelFactory(repository))
            .get(ExpenseViewModel::class.java)

        expenseViewModel.getExpenseItemsByStatus("Expense").observe(viewLifecycleOwner, Observer { expenses ->
            adapter.setExpenses(expenses)
        })

        return view
    }
}