package com.example.expensecontrol.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensecontrol.databinding.ItemBinding
import com.example.expensecontrol.databinding.ItemMainBinding
import com.example.expensecontrol.models.Category
import com.example.expensecontrol.models.Expense

class MainAdapter(var list: List<Category>,var onClickListener: OnClickListener) : RecyclerView.Adapter<MainAdapter.Vh>() {

    lateinit var subAdapter: SubAdapter

    inner class Vh(var itemMainBinding: ItemMainBinding) :
        RecyclerView.ViewHolder(itemMainBinding.root) {
        fun onBind(category: Category) {
            subAdapter = SubAdapter(category.expenseList,object : SubAdapter.OnMyItemClickListener{
                override fun onMyItemClick(expense: Expense) {
                    onClickListener.onItemClick(expense)
                }
            })
            itemMainBinding.rv.adapter = subAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onItemClick(expense: Expense)
    }
}