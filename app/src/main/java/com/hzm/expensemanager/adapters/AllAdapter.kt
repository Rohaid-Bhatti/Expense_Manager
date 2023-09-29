package com.hzm.expensemanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hzm.expensemanager.R
import com.hzm.expensemanager.roomDB.ExpensesEntity

class AllAdapter : RecyclerView.Adapter<AllAdapter.ViewHolder>() {
    private var expensesList: List<ExpensesEntity> = emptyList()

    fun setExpenses(expenses: List<ExpensesEntity>) {
        expensesList = expenses
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define UI elements in the item view (e.g., TextViews)
        val amountTextView: TextView = itemView.findViewById(R.id.textAmount)
        val statusTextView: TextView = itemView.findViewById(R.id.textStatus)
        val reasonTextView: TextView = itemView.findViewById(R.id.textReason)
        val DateTextView: TextView = itemView.findViewById(R.id.textDate)
        // Add other TextViews for other expense details here.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expensesList[position]
        holder.amountTextView.text = "Amount: ${expense.amount}"
        holder.statusTextView.text = "Status: ${expense.status}"
        holder.reasonTextView.text = "Reason: ${expense.reason}"
        holder.DateTextView.text = "Date: ${expense.createdDate}"
        // Bind other data fields here.
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

}