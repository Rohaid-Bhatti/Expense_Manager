package com.hzm.expensemanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hzm.expensemanager.R
import com.hzm.expensemanager.roomDB.ExpensesEntity

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    private var expenseList: List<ExpensesEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.expense_item, // Replace with your expense_item.xml layout
            parent,
            false
        )
        return ExpenseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun setExpenses(expenses: List<ExpensesEntity>) {
        expenseList = expenses
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your views here using itemView.findViewById() or DataBinding.
        private val textAmount: TextView = itemView.findViewById(R.id.textAmount)
        private val textReason: TextView = itemView.findViewById(R.id.textReason)
        private val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)
        fun bind(expense: ExpensesEntity) {
            // Bind expense data to your item views.
            // Example: itemView.findViewById<TextView>(R.id.textAmount).text = expense.amount
            textAmount.text = "Amount: ${expense.amount}"
            textReason.text = "Amount: ${expense.reason}"
            textStatus.text = "Status: ${expense.status}"
            textDate.text = "Date: ${expense.createdDate}" // Format date as needed
        }
    }
}