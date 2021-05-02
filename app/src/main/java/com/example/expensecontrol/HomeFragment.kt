package com.example.expensecontrol

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expensecontrol.adapters.MainAdapter
import com.example.expensecontrol.databinding.CustomDialogBinding
import com.example.expensecontrol.databinding.FragmentHomeBinding
import com.example.expensecontrol.db.AppDatabase
import com.example.expensecontrol.models.CalcGeneration
import com.example.expensecontrol.models.Category
import com.example.expensecontrol.models.Expense
import com.example.expensecontrol.models.InitializationClass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var appDatabase: AppDatabase
    lateinit var mainAdapter: MainAdapter
    lateinit var categoryList: ArrayList<Category>
    lateinit var transports: ArrayList<Expense>
    lateinit var eatings: ArrayList<Expense>
    lateinit var shoppings: ArrayList<Expense>
    lateinit var kommunals: ArrayList<Expense>
    lateinit var sharedPreferences: SharedPreferences
    var oldIcon: Int = 1
    var oldTitle: String = ""
    var oldColor: String = ""
    var isNext = 1
    var comment = ""
    var counting = ""
    private val TAG = "HomeFragment"

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getInstance(requireContext())

        val date = getDate()

        binding.date.text = date

        binding.next.setOnClickListener {
            if (isNext == 1) {
                binding.icon.visibility = View.GONE
                binding.icon.setImageResource(R.drawable.ic_add)
                binding.title.text = "Boshqa"
                binding.title.setTextColor(Color.parseColor("#000000"))
                binding.next.setImageResource(R.drawable.ic_baseline_arrow_back_ios_new_24)
                isNext = 2
            } else {
                binding.icon.visibility = View.VISIBLE
                binding.icon.setImageResource(oldIcon)
                binding.title.text = oldTitle
                binding.title.setTextColor(Color.parseColor(oldColor))
                binding.next.setImageResource(R.drawable.ic_next)
                isNext = 1
            }
        }

        binding.comment.setOnClickListener {
            val customBinding = CustomDialogBinding.inflate(layoutInflater)
            val create = AlertDialog.Builder(requireContext()).create()
            create.setView(customBinding.root)
            customBinding.ok.setOnClickListener {
                comment = customBinding.text.text.toString()
                create.dismiss()
                Toast.makeText(requireContext(), "Izoh saqlandi", Toast.LENGTH_SHORT).show()
            }
            customBinding.cancel.setOnClickListener {
                create.dismiss()
            }
            create.show()
        }

        binding.minus.setOnClickListener {
            if (counting.isNotEmpty()) {
                if (counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting != "0") {
                    equaling()
                    val time = getTime()
                    val value = binding.value.text.toString()
                    val category = binding.title.text.toString()
                    val transaction = InitializationClass.initTrans(
                        category,
                        value.toDouble(),
                        true,
                        comment,
                        date,
                        time,
                        getDay(),
                        Calendar.WEEK_OF_MONTH,
                        getMonth()
                    )
                    appDatabase.transactionDao().addTransaction(transaction)
                    comment = ""
                    counting = ""
                    binding.value.setText("0")
                    Toast.makeText(requireContext(), "Chiqim saqlandi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.plus.setOnClickListener {
            if (counting.isNotEmpty()) {
                if (counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting != "0") {
                    equaling()
                    val time = getTime()
                    val value = binding.value.text.toString()
                    val transaction = InitializationClass.initTrans(
                        "Kirim",
                        value.toDouble(),
                        false,
                        comment,
                        date,
                        time,
                        getDay(),
                        Calendar.WEEK_OF_MONTH,
                        getMonth()
                    )
                    appDatabase.transactionDao().addTransaction(transaction)
                    comment = ""
                    counting = ""
                    binding.value.setText("0")
                    Toast.makeText(requireContext(), "Kirim saqlandi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.equalsDone.setOnClickListener {
            equaling()
        }

        sharedPreferences = requireContext().getSharedPreferences("EXPENSE", MODE_PRIVATE)
        val string = sharedPreferences.getString("isFirst", "false")

        if (string == "true") {
            binding.splash.visibility = View.GONE
            binding.splash1.visibility = View.GONE
        }

        binding.splash.setOnClickListener {
            if (string == "false") {
                binding.splash.visibility = View.GONE
                binding.splash1.visibility = View.GONE
                sharedPreferences.edit().putString("isFirst", "true").apply()
            }
        }
        binding.splash1.setOnClickListener {
            if (string == "false") {
                binding.splash.visibility = View.GONE
                binding.splash1.visibility = View.GONE
                sharedPreferences.edit().putString("isFirst", "true").apply()
            }
        }

        //calculator operations

        binding.number0.setOnClickListener(this)
        binding.number1.setOnClickListener(this)
        binding.number2.setOnClickListener(this)
        binding.number3.setOnClickListener(this)
        binding.number4.setOnClickListener(this)
        binding.number5.setOnClickListener(this)
        binding.number6.setOnClickListener(this)
        binding.number7.setOnClickListener(this)
        binding.number8.setOnClickListener(this)
        binding.number9.setOnClickListener(this)
        binding.dot.setOnClickListener(this)
        binding.calcPlus.setOnClickListener(this)
        binding.calcMinus.setOnClickListener(this)
        binding.calcDivide.setOnClickListener(this)
        binding.calcMore.setOnClickListener(this)
        binding.removeLast.setOnClickListener(this)
        binding.clear.setOnClickListener(this)

        //expense categories adapter
        mainAdapter = MainAdapter(loadCategory(), object : MainAdapter.OnClickListener {
            override fun onItemClick(expense: Expense) {
                binding.icon.setImageResource(expense.iconId!!)
                binding.title.text = expense.title
                binding.title.setTextColor(Color.parseColor(expense.color))
                oldColor = expense.color!!
                oldIcon = expense.iconId!!
                oldTitle = expense.title!!
                if (isNext == 3) {
                    isNext = 2
                    binding.next.setImageResource(R.drawable.ic_baseline_arrow_back_ios_new_24)
                } else {
                    isNext = 1
                    binding.next.setImageResource(R.drawable.ic_next)
                }
            }
        })
        binding.rv.adapter = mainAdapter

        binding.statistics.setOnClickListener {
            findNavController().navigate(R.id.statisticsFragment)
        }

        //calculator symbols hiding/showing
        binding.calc.setOnClickListener {
            if (binding.calcPlus.visibility == View.INVISIBLE) {
                binding.calcPlus.visibility = View.VISIBLE
                binding.calcMore.visibility = View.VISIBLE
                binding.calcMinus.visibility = View.VISIBLE
                binding.calcDivide.visibility = View.VISIBLE
            } else {
                binding.calcPlus.visibility = View.INVISIBLE
                binding.calcMore.visibility = View.INVISIBLE
                binding.calcMinus.visibility = View.INVISIBLE
                binding.calcDivide.visibility = View.INVISIBLE
            }
        }

        return binding.root
    }

    private fun equaling() {
        if (counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×') {
            val quantity = CalcGeneration.bill(counting)
            if (quantity.toString().substring(quantity.toString().lastIndex - 1) == ".0") {
                counting = quantity.toString().substring(0, quantity.toString().lastIndex - 1)
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            } else {
                counting = CalcGeneration.bill(counting).toString()
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        return simpleDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(date)
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

    private fun loadCategory(): ArrayList<Category> {
        categoryList = ArrayList()
        categoryList.add(Category("Transport", loadTransports()))
        categoryList.add(Category("Taomlanish", loadEating()))
        categoryList.add(Category("Bozor", loadShopping()))
        categoryList.add(Category("Kommunal", loadKommunal()))
        return categoryList
    }

    private fun loadKommunal(): List<Expense> {
        kommunals = ArrayList()
        kommunals.add(Expense("#00C221", "Kommunal", R.drawable.ic_kommunal))
        kommunals.add(Expense("#00C221", "Elektr", R.drawable.ic_electr))
        kommunals.add(Expense("#00C221", "Gaz", R.drawable.ic_gas))
        kommunals.add(Expense("#00C221", "Suv", R.drawable.ic_water))
        return kommunals
    }

    private fun loadShopping(): List<Expense> {
        shoppings = ArrayList()
        shoppings.add(Expense("#FF000C", "Bozor", R.drawable.ic_shopping))
        shoppings.add(Expense("#FF000C", "Kiyim-kechak", R.drawable.ic_fashion))
        shoppings.add(Expense("#FF000C", "Oziq-ovqat", R.drawable.ic_vegetables))
        shoppings.add(Expense("#FF000C", "Texnika", R.drawable.ic_tech))
        return shoppings
    }

    private fun loadEating(): List<Expense> {
        eatings = ArrayList()
        eatings.add(Expense("#B90087", "Taomlanish", R.drawable.ic_dish))
        eatings.add(Expense("#B90087", "Fast Food", R.drawable.ic_fast_food))
        eatings.add(Expense("#B90087", "Pitsa", R.drawable.ic_pizza))
        eatings.add(Expense("#B90087", "Milliy Taom", R.drawable.ic_restaurant))
        return eatings
    }

    private fun loadTransports(): List<Expense> {
        transports = ArrayList()
        transports.add(Expense("#0277BD", "Transport", R.drawable.ic_transportation))
        transports.add(Expense("#0277BD", "Taxi", R.drawable.ic_taxi))
        transports.add(Expense("#0277BD", "Metro", R.drawable.ic_tram))
        transports.add(Expense("#0277BD", "Avtobus", R.drawable.ic_bus))

        oldColor = "#0277BD"
        oldIcon = R.drawable.ic_transportation
        oldTitle = "Transport"

        return transports
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.number0 -> {
                if (counting != "") {
                    counting += "0"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.number1 -> {
                counting += "1"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number2 -> {
                counting += "2"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number3 -> {
                counting += "3"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number4 -> {
                counting += "4"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number5 -> {
                counting += "5"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number6 -> {
                counting += "6"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number7 -> {
                counting += "7"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number8 -> {
                counting += "8"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.number9 -> {
                counting += "9"
                binding.value.setText(counting)
                binding.value.setSelection(binding.value.text.toString().length)
            }
            R.id.dot -> {
                if (counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×') {
                    if (CalcGeneration.isNotDoubleDot(counting)) {
                        counting += "."
                        binding.value.setText(counting)
                        binding.value.setSelection(binding.value.text.toString().length)
                    }
                }
            }
            R.id.calc_plus -> {
                if (counting != "" && counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting.last() != '.') {
                    counting += "+"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.calc_minus -> {
                if (counting != "" && counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting.last() != '.') {
                    counting += "-"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                } else if (counting == "") {
                    counting = "-"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.calc_divide -> {
                if (counting != "" && counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting.last() != '.') {
                    counting += "÷"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.calc_more -> {
                if (counting != "" && counting.last() != '-' && counting.last() != '+' && counting.last() != '÷' && counting.last() != '×' && counting.last() != '.') {
                    counting += "×"
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.remove_last -> {
                if (counting != "" && counting.length != 1) {
                    counting = counting.substring(0, counting.lastIndex)
                    binding.value.setText(counting)
                    binding.value.setSelection(binding.value.text.toString().length)
                } else if (counting.length == 1) {
                    counting = ""
                    binding.value.setText("0")
                    binding.value.setSelection(binding.value.text.toString().length)
                }
            }
            R.id.clear -> {
                counting = ""
                binding.value.setText("0")
            }
            R.id.equals_done -> {

            }
        }
    }

}