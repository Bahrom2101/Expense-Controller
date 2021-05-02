package com.example.expensecontrol

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensecontrol.databinding.FragmentInfoBinding
import com.example.expensecontrol.db.AppDatabase
import com.example.expensecontrol.models.CalcGeneration

class InfoFragment : Fragment() {

    lateinit var binding: FragmentInfoBinding
    lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getInstance(requireContext())

        val id = arguments?.getInt("transactionId")
        val transactionById = appDatabase.transactionDao().getTransactionById(id!!)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.delete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Bu hisobotni o'chirishni hohlaysizmi ?")
            dialog.setPositiveButton(
                "Ha"
            ) { dialog, which ->
                appDatabase.transactionDao().deleteTransaction(transactionById)
                findNavController().popBackStack()
            }
            dialog.setNegativeButton(
                "Yo'q"
            ) { dialog, which -> dialog.dismiss() }
            dialog.show()
        }

        if (transactionById.isExpense == false) {
            binding.type.text = "Kirim"
            binding.type.setTextColor(Color.parseColor("#4AED11"))
        } else {
            binding.type.text = "Chiqim"
            binding.type.setTextColor(Color.parseColor("#ED1111"))
        }

        binding.category.text = transactionById.category
        binding.value.text = String.format("%,.1f", (transactionById.value))
        if (transactionById.comment != "") {
            binding.comment.text = transactionById.comment
        } else {
            binding.comment.text = "Yo'q"
        }
        binding.date.text = transactionById.date
        binding.time.text = transactionById.time

        return binding.root
    }
}