package com.example.expensecontrol.models

import com.example.expensecontrol.entity.Transaction
import java.io.Serializable

data class CategoryStatistics(var title: String, var transactionList: List<Transaction>): Serializable