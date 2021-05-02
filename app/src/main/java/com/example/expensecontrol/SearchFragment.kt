package com.example.expensecontrol

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensecontrol.adapters.RvAdapter
import com.example.expensecontrol.databinding.FragmentSearchBinding
import com.example.expensecontrol.db.AppDatabase
import com.example.expensecontrol.entity.Transaction

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var appDatabase: AppDatabase
    lateinit var transactionList: List<Transaction>
    lateinit var rvAdapter: RvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            if (binding.searchEdit.text.isNotEmpty()) {
                binding.searchEdit.setText("")
            } else {
                findNavController().popBackStack()
            }
        }

        binding.search.setOnClickListener {
            val searchText = "%${binding.searchEdit.text}%"
            if (searchText != "" && searchText.length > 3) {
                appDatabase = AppDatabase.getInstance(requireContext())
                transactionList = appDatabase.transactionDao().searchTransactions(searchText)
                rvAdapter = RvAdapter(
                    transactionList,
                    object : RvAdapter.OnMyItemClickListener {
                        override fun onMyItemClick(transaction: Transaction) {
                            val bundle = Bundle()
                            bundle.putInt("transactionId", transaction.id!!)
                            findNavController().navigate(R.id.infoFragment, bundle)
                        }
                    })

                binding.rv.adapter = rvAdapter
            }
        }

        return binding.root
    }
}