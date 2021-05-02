package com.example.expensecontrol.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensecontrol.databinding.ItemBinding
import com.example.expensecontrol.models.Expense

class SubAdapter(var list: List<Expense>, var onMyItemClickListener: OnMyItemClickListener) :
    RecyclerView.Adapter<SubAdapter.Vh>() {
    inner class Vh(var itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(expense: Expense) {
            itemBinding.icon.setImageResource(expense.iconId!!)
            itemBinding.title.text = expense.title
            itemBinding.title.setTextColor(Color.parseColor(expense.color))
            itemBinding.root.setOnClickListener {
                onMyItemClickListener.onMyItemClick(expense)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnMyItemClickListener {
        fun onMyItemClick(expense: Expense)
    }
}