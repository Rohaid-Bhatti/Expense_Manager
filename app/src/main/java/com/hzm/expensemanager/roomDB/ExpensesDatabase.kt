package com.hzm.expensemanager.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

@Database(entities = [ExpensesEntity::class, UserEntity::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {

    abstract fun expensesDao() : ExpensesDAO
    abstract fun userDao() : UserDAO

    companion object{
        private var INSTANCE : ExpensesDatabase? = null
        fun getDatabase(context: Context) : ExpensesDatabase {
            if(INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context,
                    ExpensesDatabase::class.java,
                    "expense_manager_database").build()
                }
            }
            return INSTANCE!!
        }
    }
}