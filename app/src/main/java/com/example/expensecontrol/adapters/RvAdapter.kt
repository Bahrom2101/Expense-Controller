package com.example.expensecontrol.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensecontrol.R
import com.example.expensecontrol.databinding.ItemStatisticsBinding
import com.example.expensecontrol.entity.Transaction
import java.text.SimpleDateFormat

class RvAdapter(
    var list: List<Transaction>,
    var onMyItemClickListener: OnMyItemClickListener
) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemStatisticsBinding: ItemStatisticsBinding) :
        RecyclerView.ViewHolder(itemStatisticsBinding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun onBind(transaction: Transaction) {
            itemStatisticsBinding.root.setOnClickListener {
                onMyItemClickListener.onMyItemClick(transaction)
            }
            itemStatisticsBinding.date.text = transaction.date
            itemStatisticsBinding.title.text = transaction.category
            if (transaction.comment == "") {
                itemStatisticsBinding.comment.text = "Izoh: Yo'q"
            } else {
                itemStatisticsBinding.comment.text = "Izoh: ${transaction.comment}"
            }
            if (!transaction.isExpense!!) {
                itemStatisticsBinding.value.setTextColor(Color.parseColor("#4AED11"))
                itemStatisticsBinding.value.text = "+${String.format("%(,.0f",transaction.value!!)}"
            } else {
                itemStatisticsBinding.value.setTextColor(Color.parseColor("#ED1111"))
                itemStatisticsBinding.value.text = "-${String.format("%(,.0f",transaction.value!!)}"
            }
            when (transaction.category) {
                "Transport" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_transportation)
                }
                "Taxi" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_taxi)
                }
                "Metro" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_tram)
                }
                "Avtobus" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_bus)
                }
                "Taomlanish" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_dish)
                }
                "Fast Food" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_fast_food)
                }
                "Pitsa" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_pizza)
                }
                "Milliy Taom" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_restaurant)
                }
                "Bozor" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_shopping)
                }
                "Kiyim-kechak" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_fashion)
                }
                "Oziq-ovqat" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_vegetables)
                }
                "Texnika" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_tech)
                }
                "Kommunal" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_kommunal)
                }
                "Elektr" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_electr)
                }
                "Gaz" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_gas)
                }
                "Suv" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_water)
                }
                "Boshqa" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_add)
                }
                "Kirim" -> {
                    itemStatisticsBinding.icon.setImageResource(R.drawable.ic_money)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemStatisticsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnMyItemClickListener {
        fun onMyItemClick(transaction: Transaction)
    }
}