/*
 * Created by Muhamad Syafii
 * Friday, 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import id.syafii.ataskcalculator.data.Response
import id.syafii.ataskcalculator.data.repository.Repository
import id.syafii.ataskcalculator.utils.toLiveData
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _calculateAnswer = MutableLiveData<String>()
    val calculateAnswer = _calculateAnswer.toLiveData()
    private val _calculateQuestion = MutableLiveData<String>()
    val calculateQuestion = _calculateQuestion.toLiveData()

    fun calculateFromImage(image: InputImage) = viewModelScope.launch {
        try {
            when (val result = repository.calculate(image)) {
                is Response.Success -> {
                    result.data.let { data ->
                        _calculateQuestion.postValue(data)
                        calculateFromRecognizedText(data)
                    }
                }

                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _calculateAnswer.postValue("Failed to calculate")
        }
    }

    private fun calculateFromRecognizedText(recognizedText: String) {
        val extractedExpression = extractExpressionFromText(recognizedText)
        val result = calculateArithmeticAnswer(extractedExpression)
        _calculateAnswer.postValue(result)
    }

    private fun extractExpressionFromText(text: String): String {
        // Remove any non-mathematical characters and whitespace
        return text.replace(Regex("[^0-9\\+\\-\\*/]"), "").trim()
    }


    private fun calculateArithmeticAnswer(expression: String): String {
        val operators = arrayOf("+", "-", "*", "/")
        for (operator in operators) {
            if (expression.contains(operator)) {
                val format = expression.split(operator)
                val operand1 = format[0].trim().toInt()
                val operand2 = format[1].trim().toInt()
                return when (operator) {
                    "+" -> (operand1 + operand2).toString()
                    "-" -> (operand1 - operand2).toString()
                    "*" -> (operand1 * operand2).toString()
                    "/" -> if (operand2 != 0) (operand1 / operand2).toString() else "Division by zero"
                    else -> "Invalid operator"
                }
            }
        }
        return "Invalid input!"
    }
}