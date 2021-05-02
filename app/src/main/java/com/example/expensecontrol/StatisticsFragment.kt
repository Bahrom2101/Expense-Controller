package com.example.expensecontrol

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.expensecontrol.adapters.PagerAdapter
import com.example.expensecontrol.databinding.CustomFilterDialogBinding
import com.example.expensecontrol.databinding.FragmentStatisticsBinding
import com.example.expensecontrol.databinding.TabItemBinding
import com.example.expensecontrol.db.AppDatabase
import com.example.expensecontrol.entity.Transaction
import com.example.expensecontrol.models.CategoryStatistics
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatisticsFragment : Fragment() {

    lateinit var binding: FragmentStatisticsBinding
    lateinit var appDatabase: AppDatabase
    lateinit var categoryStatisticsDay: List<Transaction>
    lateinit var categoryStatisticsWeek: List<Transaction>
    lateinit var categoryStatisticsMonth: List<Transaction>
    lateinit var categoryStatisticsAll: List<Transaction>
    lateinit var categoryStatisticsIncomeDay: List<Transaction>
    lateinit var categoryStatisticsIncomeWeek: List<Transaction>
    lateinit var categoryStatisticsIncomeMonth: List<Transaction>
    lateinit var categoryStatisticsIncomeAll: List<Transaction>
    lateinit var categoryStatisticsList: ArrayList<CategoryStatistics>
    lateinit var pagerAdapter: PagerAdapter
    var selectedType = ""
    var subSelectedType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getInstance(requireContext())

        val date = getDate()

        binding.date.text = date
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
//        binding.filter.setOnClickListener {
//            selectedType = ""
//            subSelectedType = ""
//            val dialogBinding = CustomFilterDialogBinding.inflate(layoutInflater)
//            val dialog = AlertDialog.Builder(requireContext()).create()
//            dialog.setView(dialogBinding.root)
//            dialogBinding.outcome.setOnClickListener {
//                if (selectedType == "Kirim") {
//                    dialogBinding.income.setBackgroundResource(R.drawable.shape_income)
//                    dialogBinding.income.setTextColor(Color.parseColor("#00FB23"))
//                    dialogBinding.outcome.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.outcome.setBackgroundResource(R.drawable.shape_outcome1)
//                    dialogBinding.transport.setTextColor(Color.parseColor("#3A2BE9"))
//                    dialogBinding.taomlanish.setTextColor(Color.parseColor("#3A2BE9"))
//                    dialogBinding.bozor.setTextColor(Color.parseColor("#3A2BE9"))
//                    dialogBinding.kommunal.setTextColor(Color.parseColor("#3A2BE9"))
//                    selectedType = "Chiqim"
//                } else if (selectedType == "") {
//                    dialogBinding.outcome.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.outcome.setBackgroundResource(R.drawable.shape_outcome1)
//                    selectedType = "Chiqim"
//                }
//            }
//            dialogBinding.income.setOnClickListener {
//                when (subSelectedType) {
//                    "Taomlanish" -> {
//                        dialogBinding.taomlanish.setBackgroundResource(R.drawable.shape_expense)
//                        dialogBinding.taomlanish.setTextColor(Color.parseColor("#3A2BE9"))
//                        dialogBinding.taomlanish.setCompoundDrawablesWithIntrinsicBounds(
//                            R.drawable.ic_dish1,
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                    "Transport" -> {
//                        dialogBinding.transport.setBackgroundResource(R.drawable.shape_expense)
//                        dialogBinding.transport.setTextColor(Color.parseColor("#3A2BE9"))
//                        dialogBinding.transport.setCompoundDrawablesWithIntrinsicBounds(
//                            R.drawable.ic_transportation1,
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                    "Bozor" -> {
//                        dialogBinding.bozor.setBackgroundResource(R.drawable.shape_expense)
//                        dialogBinding.bozor.setTextColor(Color.parseColor("#3A2BE9"))
//                        dialogBinding.bozor.setCompoundDrawablesWithIntrinsicBounds(
//                            R.drawable.ic_shopping1,
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                    "Kommunal" -> {
//                        dialogBinding.kommunal.setBackgroundResource(R.drawable.shape_expense)
//                        dialogBinding.kommunal.setTextColor(Color.parseColor("#3A2BE9"))
//                        dialogBinding.kommunal.setCompoundDrawablesWithIntrinsicBounds(
//                            R.drawable.ic_kommunal1,
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                }
//                if (selectedType == "Chiqim") {
//                    dialogBinding.outcome.setTextColor(Color.parseColor("#FF000C"))
//                    dialogBinding.outcome.setBackgroundResource(R.drawable.shape_outcome)
//                    dialogBinding.income.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.income.setBackgroundResource(R.drawable.shape_income1)
//                    dialogBinding.transport.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.taomlanish.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.bozor.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.kommunal.setTextColor(Color.parseColor("#63000000"))
//                    selectedType = "Kirim"
//                } else if (selectedType == "") {
//                    dialogBinding.income.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.income.setBackgroundResource(R.drawable.shape_income1)
//                    dialogBinding.transport.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.taomlanish.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.bozor.setTextColor(Color.parseColor("#63000000"))
//                    dialogBinding.kommunal.setTextColor(Color.parseColor("#63000000"))
//                    selectedType = "Kirim"
//                }
//            }
//
//            dialogBinding.transport.setOnClickListener {
//                if (selectedType != "Kirim" && selectedType != "") {
//                    when (subSelectedType) {
//                        "Taomlanish" -> {
//                            dialogBinding.taomlanish.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.taomlanish.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.taomlanish.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_dish1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Bozor" -> {
//                            dialogBinding.bozor.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.bozor.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.bozor.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_shopping1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Kommunal" -> {
//                            dialogBinding.kommunal.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.kommunal.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.kommunal.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_kommunal1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                    }
//                    dialogBinding.transport.setBackgroundResource(R.drawable.shape_expense1)
//                    dialogBinding.transport.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.transport.setCompoundDrawablesWithIntrinsicBounds(
//                        R.drawable.one,
//                        0,
//                        0,
//                        0
//                    )
//                    subSelectedType = "Transport"
//                }
//            }
//            dialogBinding.taomlanish.setOnClickListener {
//                if (selectedType != "Kirim" && selectedType != "") {
//                    when (subSelectedType) {
//                        "Transport" -> {
//                            dialogBinding.transport.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.transport.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.transport.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_transportation1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Bozor" -> {
//                            dialogBinding.bozor.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.bozor.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.bozor.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_shopping1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Kommunal" -> {
//                            dialogBinding.kommunal.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.kommunal.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.kommunal.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_kommunal1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                    }
//                    dialogBinding.taomlanish.setBackgroundResource(R.drawable.shape_expense1)
//                    dialogBinding.taomlanish.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.taomlanish.setCompoundDrawablesWithIntrinsicBounds(
//                        R.drawable.two,
//                        0,
//                        0,
//                        0
//                    )
//                    subSelectedType = "Taomlanish"
//                }
//            }
//            dialogBinding.bozor.setOnClickListener {
//                if (selectedType != "Kirim" && selectedType != "") {
//                    when (subSelectedType) {
//                        "Transport" -> {
//                            dialogBinding.transport.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.transport.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.transport.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_transportation1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Taomlanish" -> {
//                            dialogBinding.taomlanish.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.taomlanish.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.taomlanish.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_dish1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Kommunal" -> {
//                            dialogBinding.kommunal.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.kommunal.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.kommunal.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_kommunal1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                    }
//                    dialogBinding.bozor.setBackgroundResource(R.drawable.shape_expense1)
//                    dialogBinding.bozor.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.bozor.setCompoundDrawablesWithIntrinsicBounds(
//                        R.drawable.three,
//                        0,
//                        0,
//                        0
//                    )
//                    subSelectedType = "Bozor"
//                }
//            }
//            dialogBinding.kommunal.setOnClickListener {
//                if (selectedType != "Kirim" && selectedType != "") {
//                    when (subSelectedType) {
//                        "Transport" -> {
//                            dialogBinding.transport.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.transport.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.transport.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_transportation1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Taomlanish" -> {
//                            dialogBinding.taomlanish.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.taomlanish.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.taomlanish.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_dish1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                        "Bozor" -> {
//                            dialogBinding.bozor.setBackgroundResource(R.drawable.shape_expense)
//                            dialogBinding.bozor.setTextColor(Color.parseColor("#3A2BE9"))
//                            dialogBinding.bozor.setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_shopping1,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//                    }
//                    dialogBinding.kommunal.setBackgroundResource(R.drawable.shape_expense1)
//                    dialogBinding.kommunal.setTextColor(Color.parseColor("#FFFFFF"))
//                    dialogBinding.kommunal.setCompoundDrawablesWithIntrinsicBounds(
//                        R.drawable.four,
//                        0,
//                        0,
//                        0
//                    )
//                    subSelectedType = "Kommunal"
//                }
//            }
//
//            dialogBinding.filter.setOnClickListener {
//                loadFilter()
//                dialog.dismiss()
//                pagerAdapter = PagerAdapter(categoryStatisticsList, requireActivity().supportFragmentManager)
//                pagerAdapter.notifyDataSetChanged()
//                binding.viewPager.adapter = pagerAdapter
//                binding.tabLayout.setupWithViewPager(binding.viewPager)
//                setTabs()
//            }
//            dialog.show()
//        }

        loadData()

        pagerAdapter =
            PagerAdapter(categoryStatisticsList, requireActivity().supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setTabs()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.setBackgroundColor(Color.parseColor("#9574D2"))
                val findViewById = tab?.customView?.findViewById<TextView>(R.id.title_tab)
                findViewById?.setTextColor(Color.parseColor("#FFFFFF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.setBackgroundColor(Color.parseColor("#D1C8E7"))
                val findViewById = tab?.customView?.findViewById<TextView>(R.id.title_tab)
                findViewById?.setTextColor(Color.parseColor("#000000"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        loadData()

        pagerAdapter =
            PagerAdapter(categoryStatisticsList, requireActivity().supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setTabs()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.setBackgroundColor(Color.parseColor("#9574D2"))
                val findViewById = tab?.customView?.findViewById<TextView>(R.id.title_tab)
                findViewById?.setTextColor(Color.parseColor("#FFFFFF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.setBackgroundColor(Color.parseColor("#D1C8E7"))
                val findViewById = tab?.customView?.findViewById<TextView>(R.id.title_tab)
                findViewById?.setTextColor(Color.parseColor("#000000"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun loadFilter() {
        if (selectedType == "Chiqim") {
            when (subSelectedType) {
                "Transport" -> {

                }
                "Taomlanish" -> {

                }
                "Bozor" -> {

                }
                "Kommunal" -> {

                }
            }
        } else if (selectedType == "Kirim") {
            categoryStatisticsList.clear()
            categoryStatisticsIncomeAll = appDatabase.transactionDao().getAllTransactionsIncome()
            categoryStatisticsIncomeMonth =
                appDatabase.transactionDao().getTransactionsIncomeMonth(getMonth())
            categoryStatisticsIncomeWeek =
                appDatabase.transactionDao().getTransactionsIncomeWeek(Calendar.WEEK_OF_MONTH)
            categoryStatisticsIncomeDay =
                appDatabase.transactionDao().getTransactionsIncomeDay(getDay())

            categoryStatisticsList.add(CategoryStatistics("Kunlik", categoryStatisticsIncomeDay))
            categoryStatisticsList.add(CategoryStatistics("Haftalik", categoryStatisticsIncomeWeek))
            categoryStatisticsList.add(CategoryStatistics("Oylik", categoryStatisticsIncomeMonth))
            categoryStatisticsList.add(CategoryStatistics("Barchasi", categoryStatisticsIncomeAll))

        }
    }

    private fun setTabs() {
        val tabCount = binding.tabLayout.tabCount
        for (i in 0 until tabCount) {
            val tabView =
                TabItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.tabLayout.getTabAt(i)
            tabAt?.customView = tabView.root
            tabView.titleTab.text = categoryStatisticsList[i].title
            val layoutParams: ViewGroup.LayoutParams = TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            tabView.titleTab.layoutParams = layoutParams
            if (i == 0) {
                tabView.titleTab.setBackgroundColor(Color.parseColor("#9574D2"))
                tabView.titleTab.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                tabView.titleTab.setTextColor(Color.parseColor("#000000"))
                tabView.titleTab.setBackgroundColor(Color.parseColor("#D1C8E7"))
            }
        }
    }

    private fun loadData() {
        categoryStatisticsList = ArrayList()
        categoryStatisticsList.clear()
        categoryStatisticsAll = appDatabase.transactionDao().getAllTransactions()
        categoryStatisticsMonth = appDatabase.transactionDao().getTransactionsMonth(getMonth())
        categoryStatisticsWeek =
            appDatabase.transactionDao().getTransactionsWeek(Calendar.WEEK_OF_MONTH)
        categoryStatisticsDay = appDatabase.transactionDao().getTransactionsDay(getDay())

        categoryStatisticsList.add(CategoryStatistics("Kunlik", categoryStatisticsDay))
        categoryStatisticsList.add(CategoryStatistics("Haftalik", categoryStatisticsWeek))
        categoryStatisticsList.add(CategoryStatistics("Oylik", categoryStatisticsMonth))
        categoryStatisticsList.add(CategoryStatistics("Barchasi", categoryStatisticsAll))
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

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val format = simpleDateFormat.format(date)
        return format
    }

}