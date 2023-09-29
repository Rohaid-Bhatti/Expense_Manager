package com.hzm.expensemanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hzm.expensemanager.fragments.AllFragment
import com.hzm.expensemanager.fragments.ExpenseFragment
import com.hzm.expensemanager.fragments.IncomeFragment

class FragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3  // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment()
            1 -> ExpenseFragment()
            2 -> IncomeFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}