package com.example.expensecontrol.models

import kotlin.collections.ArrayList

object CalcGeneration {

    fun valueToShortSum(number: Double): String {
        return if (number < 1000000) {
            "${String.format("%,.1f", (number / 1000))} ming"
        } else {
            "${String.format("%.1f", (number / 1000000))} mln"
        }
    }

    fun isNotDoubleDot(s: String): Boolean {
        var value = s
        for (i in s.length - 1 downTo 0) {
            if (s[i] == '-' || s[i] == '+' || s[i] == '÷' || s[i] == '×') {
                value = s.substring(i)
                break
            }
        }
        return !value.contains('.')
    }

    fun bill(s: String): Double {
        val numbers = ArrayList<Double>()
        val str = prepareBill(s + "+")
        var index = -1
        var lastOperation = ""
        for (i in 1 until str.length) {
            if (str[i] == '-' || str[i] == '+') {
                numbers.add(str.substring(0, i).toDouble())
                break
            }
        }
        for (i in 1 until str.length) {
            if (str[i] == '+') {
                if (index > -1) {
                    numbers.add("$lastOperation${str.substring(index + 1, i)}".toDouble())
                }
                index = i
                lastOperation = "+"
            } else if (str[i] == '-') {
                if (index > -1) {
                    numbers.add("$lastOperation${str.substring(index + 1, i)}".toDouble())
                }
                index = i
                lastOperation = "-"
            }
        }
        var value = 0.0
        for (number in numbers) {
            value += number
        }
        return value
    }

    private fun prepareBill(s: String): String {
        return if (!(s.contains("÷") || s.contains("×"))) {
            s
        } else {
            var str = s
            val indexDiv = s.indexOf("÷")
            val indexMore = s.indexOf("×")
            if (indexDiv > -1) {
                val untilDiv = s.substring(0, indexDiv)
                var untilDivOperation = -1
                val nextDiv = s.substring(indexDiv + 1)
                var nextDivOperation = -1
                for (i in untilDiv.length - 1 downTo 0) {
                    if (untilDiv.toCharArray()[i] == '-' || untilDiv.toCharArray()[i] == '+') {
                        untilDivOperation = i
                        break
                    }
                }
                for (i in nextDiv.indices) {
                    if (nextDiv.toCharArray()[i] == '-' || nextDiv.toCharArray()[i] == '+') {
                        nextDivOperation = i + indexDiv + 1
                        break
                    }
                }
                val number1 = s.substring(untilDivOperation + 1, indexDiv).toDouble()
                val number2 = s.substring(indexDiv + 1, nextDivOperation).toDouble()
                str = s.substring(
                    0,
                    untilDivOperation + 1
                ) + (number1 / number2).toString() + s.substring(nextDivOperation)
            } else if (indexMore > -1) {
                val untilMore = s.substring(0, indexMore)
                var untilMoreOperation = -1
                val nextMore = s.substring(indexMore + 1)
                var nextMoreOperation = -1
                for (i in untilMore.length - 1 downTo 0) {
                    if (untilMore.toCharArray()[i] == '-' || untilMore.toCharArray()[i] == '+') {
                        untilMoreOperation = i
                        break
                    }
                }
                for (i in nextMore.indices) {
                    if (nextMore.toCharArray()[i] == '-' || nextMore.toCharArray()[i] == '+') {
                        nextMoreOperation = i + indexMore + 1
                        break
                    }
                }
                val number1 = s.substring(untilMoreOperation + 1, indexMore).toDouble()
                val number2 = s.substring(indexMore + 1, nextMoreOperation).toDouble()
                str = s.substring(
                    0,
                    untilMoreOperation + 1
                ) + (number1 * number2).toString() + s.substring(nextMoreOperation)
            }
            prepareBill(str)
        }
    }

}