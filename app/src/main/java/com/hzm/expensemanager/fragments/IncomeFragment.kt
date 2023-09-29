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
import com.hzm.expensemanager.adapters.IncomeAdapter
import com.hzm.expensemanager.respository.ExpenseRepository
import com.hzm.expensemanager.roomDB.ExpensesDatabase
import com.hzm.expensemanager.viewModels.ExpenseViewModel
import com.hzm.expensemanager.viewModels.ExpenseViewModelFactory

class IncomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IncomeAdapter
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_income, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = IncomeAdapter()
        recyclerView.adapter = adapter

        val dao = ExpensesDatabase.getDatabase(requireContext()).expensesDao()
        val repository = ExpenseRepository(dao)

        expenseViewModel = ViewModelProvider(this, ExpenseViewModelFactory(repository))
            .get(ExpenseViewModel::class.java)

        expenseViewModel.getExpenseItemsByStatus("Income").observe(viewLifecycleOwner, Observer { expenses ->
            adapter.setExpenses(expenses)
        })

        return view
    }
}