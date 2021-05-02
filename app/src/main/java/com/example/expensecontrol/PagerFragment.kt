package com.example.expensecontrol

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensecontrol.adapters.RvAdapter
import com.example.expensecontrol.databinding.FragmentPagerBinding
import com.example.expensecontrol.db.AppDatabase
import com.example.expensecontrol.entity.Transaction
import com.example.expensecontrol.models.CalcGeneration
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"

class PagerFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var binding: FragmentPagerBinding
    lateinit var rvAdapter: RvAdapter
    lateinit var appDatabase: AppDatabase
    lateinit var transactionList: List<Transaction>
    lateinit var handler: Handler
    var height = 250

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getInstance(requireContext())

        when (param1) {
            "Kunlik" -> {
                transactionList = appDatabase.transactionDao().getTransactionsDay(getDay())
            }
            "Haftalik" -> {
                transactionList =
                    appDatabase.transactionDao().getTransactionsWeek(Calendar.WEEK_OF_MONTH)
            }
            "Oylik" -> {
                transactionList = appDatabase.transactionDao().getTransactionsMonth(getMonth())
            }
            "Barchasi" -> {
                transactionList = appDatabase.transactionDao().getAllTransactions()
            }
        }

        var incomeSum = 0.0
        var outcomeSum = 0.0
        for (transaction in transactionList) {
            if (transaction.isExpense!!) {
                outcomeSum += transaction.value!!
            } else {
                incomeSum += transaction.value!!
            }
        }
        var transportSum = 0.0
        var taomlanishSum = 0.0
        var bozorSum = 0.0
        var kommunal = 0.0
        var boshqaSum = 0.0

        for (transaction in transactionList) {
            if (transaction.isExpense!!)
                when (transaction.category) {
                    "Transport" -> {
                        transportSum += transaction.value!!.toDouble()
                    }
                    "Taxi" -> {
                        transportSum += transaction.value!!.toDouble()
                    }
                    "Metro" -> {
                        transportSum += transaction.value!!.toDouble()
                    }
                    "Avtobus" -> {
                        transportSum += transaction.value!!.toDouble()
                    }
                    "Taomlanish" -> {
                        taomlanishSum += transaction.value!!.toDouble()
                    }
                    "Fast Food" -> {
                        taomlanishSum += transaction.value!!.toDouble()
                    }
                    "Pitsa" -> {
                        taomlanishSum += transaction.value!!.toDouble()
                    }
                    "Milliy Taom" -> {
                        taomlanishSum += transaction.value!!.toDouble()
                    }
                    "Bozor" -> {
                        bozorSum += transaction.value!!.toDouble()
                    }
                    "Kiyim-kechak" -> {
                        bozorSum += transaction.value!!.toDouble()
                    }
                    "Oziq-ovqat" -> {
                        bozorSum += transaction.value!!.toDouble()
                    }
                    "Texnika" -> {
                        bozorSum += transaction.value!!.toDouble()
                    }
                    "Kommunal" -> {
                        kommunal += transaction.value!!.toDouble()
                    }
                    "Elektr" -> {
                        kommunal += transaction.value!!.toDouble()
                    }
                    "Gaz" -> {
                        kommunal += transaction.value!!.toDouble()
                    }
                    "Suv" -> {
                        kommunal += transaction.value!!.toDouble()
                    }
                    "Boshqa" -> {
                        boshqaSum += transaction.value!!.toDouble()
                    }
                }
        }

        binding.transportSum.text = CalcGeneration.valueToShortSum(transportSum)
        binding.taomlanishSum.text = CalcGeneration.valueToShortSum(taomlanishSum)
        binding.bozorSum.text = CalcGeneration.valueToShortSum(bozorSum)
        binding.kommunalSum.text = CalcGeneration.valueToShortSum(kommunal)
        binding.boshqaSum.text = CalcGeneration.valueToShortSum(boshqaSum)

        binding.outcome.text = String.format("%(,.0f",outcomeSum)

        val pieChart = PieChart(
            slices = provideSlices(transportSum, taomlanishSum, bozorSum, kommunal, boshqaSum),
            clickListener = null,
            sliceStartPoint = 10f,
            sliceWidth = 40f
        ).build()

        binding.chart.setPieChart(pieChart)

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

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(runnable, 100)
                }
            }
        })
        return binding.root
    }

    private var runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                binding.layout.maxHeight = height
                if (height != 0) {
                    height--
                }
                if (height >= 0) {
                    handler.postDelayed(this, 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private fun provideSlices(
            transportSum: Double,
            taomlanishSum: Double,
            bozorSum: Double,
            kommunal: Double,
            boshqaSum: Double
        ): ArrayList<Slice> {
            return arrayListOf(
                Slice(
                    transportSum.toFloat(),
                    R.color.my_color1,
                    "Transport"
                ),
                Slice(
                    taomlanishSum.toFloat(),
                    R.color.my_color2,
                    "Taomlanish"
                ),
                Slice(
                    bozorSum.toFloat(),
                    R.color.my_color3,
                    "Bozor"
                ),
                Slice(
                    kommunal.toFloat(),
                    R.color.my_color4,
                    "Kommunal"
                ),
                Slice(
                    boshqaSum.toFloat(),
                    R.color.my_color5,
                    "Other"
                )
            )
        }

        @JvmStatic
        fun newInstance(param1: String) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDay(): Int {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd")
        val format = simpleDateFormat.format(date)
        return format.toInt()
    }

    @SuppressLint("SimpleDateFormat")
    fun getMonth(): Int {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("MM")
        return simpleDateFormat.format(date).toInt()
    }
}