package com.example.expensecontrol.dao

import androidx.room.*
import com.example.expensecontrol.entity.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun addTransaction(transaction: Transaction)

    @Query("select * from `transaction` where id=:id")
    fun getTransactionById(id: Int): Transaction

    @Query("select * from `transaction` where category like :word or comment like :word")
    fun searchTransactions(word:String): List<Transaction>

    @Query("select * from `transaction`")
    fun getAllTransactions(): List<Transaction>

    @Query("select * from `transaction` where day=:day")
    fun getTransactionsDay(day: Int): List<Transaction>

    @Query("select * from `transaction` where week=:week")
    fun getTransactionsWeek(week: Int): List<Transaction>

    @Query("select * from `transaction` where month=:month")
    fun getTransactionsMonth(month: Int): List<Transaction>

    @Query("select * from `transaction` where is_expense=0")
    fun getAllTransactionsIncome(): List<Transaction>

    @Query("select * from `transaction` where day=:day and is_expense=0")
    fun getTransactionsIncomeDay(day: Int): List<Transaction>

    @Query("select * from `transaction` where week=:week and is_expense=0")
    fun getTransactionsIncomeWeek(week: Int): List<Transaction>

    @Query("select * from `transaction` where month=:month and is_expense=0")
    fun getTransactionsIncomeMonth(month: Int): List<Transaction>

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}