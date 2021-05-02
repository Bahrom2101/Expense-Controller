package com.example.expensecontrol.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Transaction {
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    @ColumnInfo(name = "category")
    var category:String? = null

    @ColumnInfo(name = "value")
    var value:Double? = null

    @ColumnInfo(name = "is_expense")
    var isExpense:Boolean? = null

    @ColumnInfo(name = "comment")
    var comment:String? = null

    @ColumnInfo(name = "date")
    var date:String? = null

    @ColumnInfo(name = "time")
    var time:String? = null

    @ColumnInfo(name = "day")
    var day:Int? = null

    @ColumnInfo(name = "week")
    var week:Int? = null

    @ColumnInfo(name = "month")
    var month:Int? = null

    override fun toString(): String {
        return "Transaction(category=$category, value=$value, date=$date, time=$time)"
    }

}