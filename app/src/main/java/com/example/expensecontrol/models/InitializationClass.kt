package com.example.expensecontrol.models

import com.example.expensecontrol.entity.Transaction

object InitializationClass {
    fun initTrans(
        category: String,
        value: Double,
        isExpense: Boolean,
        comment: String,
        date: String,
        time: String,
        day: Int,
        week: Int,
        month: Int
    ): Transaction {
        val transaction = Transaction()
        transaction.category = category
        transaction.value = value
        transaction.isExpense = isExpense
        transaction.comment = comment
        transaction.date = date
        transaction.time = time
        transaction.day = day
        transaction.week = week
        transaction.month = month
        return transaction
    }
}