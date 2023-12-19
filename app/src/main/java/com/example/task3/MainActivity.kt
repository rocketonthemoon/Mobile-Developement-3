package com.example.task3

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.task3.ui.theme.Task3Theme
import android.widget.Button
import android.widget.TextView

// potential problems to fix in future
// ## 1 ##
// special functions like sign change and square root only works on the tvWorking , doesnt work on the tvResults
// therefore , if you've done some calculations , the results which will go to the tvResults wont get affected even if we pressed the button after some calculations
//
// ## 2 ##
// when an operated operates with just the decimal term , app crashes
//
// ## 3 ##
// when the sign change is used on an operant , if tried to run the equalsHandler app crashes
//
class MainActivity : ComponentActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var tvWorkings: TextView
    private lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWorkings = findViewById(R.id.tvWorkings)
        tvResults = findViewById(R.id.tvResults)
    }

    fun numberHandler(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) tvWorkings.append(view.text)
                canAddDecimal = false
            } else {
                tvWorkings.append(view.text)
            }
            canAddOperation = true
        }
    }

    fun operationHandler(view: View) {
        if (view is Button && canAddOperation) {
            tvWorkings.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearHandler(view: View) {
        tvWorkings.text = ""
        tvResults.text = ""
    }

    fun backSpaceHandler(view: View) {
        val length = tvWorkings.length()
        if (length > 0)
            tvWorkings.text = tvWorkings.text.subSequence(0, length - 1)
    }

    fun signChangeHandler(view: View) {
        val currentValue = tvWorkings.text.toString().toDoubleOrNull()
        if (currentValue != null) {
            tvWorkings.text = (-currentValue).toString()
        }

    }

    fun equalsHandler(view: View) {
        tvResults.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }

                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in tvWorkings.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

    fun squareRootHandler(view: View) {
        val currentValue = tvWorkings.text.toString().toDoubleOrNull()
        if (currentValue != null && currentValue >= 0) {
            tvWorkings.text = Math.sqrt(currentValue).toString()
        }
    }
}




