package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
import kotlin.math.min
import kotlin.math.pow
import kotlin.text.StringBuilder

// defines the maximum amount of Chars in the calculation TextView
private const val maxAmountOfChars = 16

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCalculation: TextView = findViewById(R.id.tvCalculation)
        val tvResult: TextView = findViewById(R.id.tvResult)
 // todo do all digits like this by copying
        findViewById<Button>(R.id.button1).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && (tvCalculation.text[tvCalculation.text.lastIndex] == ')'
                            || tvCalculation.text[tvCalculation.text.lastIndex] == '%')) {
                    tvCalculation.text = tvCalculation.text.append("×1")
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = tvCalculation.text.append("1")
                } else {
                    tvCalculation.text = tvCalculation.text.append("1")
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×2").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("2").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("2").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×3").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("3").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("3").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button4).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×4").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("4").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("4").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }
// todo for all digit buttons: put case isEmpty in the first if statement separately to not have to test it later in the else if-blocks
        findViewById<Button>(R.id.button5).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×5").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("5").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("5").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button6).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×6").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("6").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("6").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button7).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×7").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("7").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("7").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button8).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×8").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()  || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("8").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("8").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button9).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("×9").toString()
                }
                // if the last Char is '0' and is the first Char of a number, remove it
                // and add the new digit, because numbers cannot have leading zeroes
                else if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit() || tvCalculation.text.numberHasComma()))
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("9").toString()
                } else {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("9").toString()
                }
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        findViewById<Button>(R.id.button0).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×0").toString()
                    tvResult.text = tvCalculation.text.calculate()
                }
                // if previous Char is '÷', just add '0', but do not calculate anything
                if (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '÷') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("0").toString()
                    tvResult.text = ""
                }
                // if last Char is already a '0' and the first digit of a number, do no not add another one
                else if ((tvCalculation.text.length == 1 && tvCalculation.text[0] == '0') ||
                    (tvCalculation.text.length > 1 && tvCalculation.text[tvCalculation.text.lastIndex] == '0'
                    && !((tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit() ||
                            tvCalculation.text.numberHasComma())))) {
                    Unit
                }
                    // todo complete for all digits 1,03 if numberHasComma() append 3
                // if previous Char is '%' append '×(0'
                else if(tvCalculation.text.isNotEmpty() &&
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("×(0").toString()
                }
                else {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("0").toString()
                    tvResult.text = tvCalculation.text.calculate()
                }

            }
        }

        // brackets button: ()
        findViewById<Button>(R.id.buttonBrackets).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // if field is is empty, last Char is operator or "(", add "("
                if (tvCalculation.text.isEmpty() ||
                    tvCalculation.text[tvCalculation.text.lastIndex].isOperator() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '('
                ) {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("(").toString()

                }
                // if last Char is a digit OR '%', append depending on bracketPicker()
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit()
                    || tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    if (tvCalculation.text.toString().bracketPicker() == "(") {
                        tvCalculation.text = StringBuilder(tvCalculation.text)
                            .append("×(").toString()
                    } else {
                        tvCalculation.text = StringBuilder(tvCalculation.text)
                            .append(")").toString()
                    }

                }
                // if last Char is ")", append depending on bracketPicker()
                else if (tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    if (tvCalculation.text.toString().bracketPicker() == "(") {
                        tvCalculation.text = StringBuilder(tvCalculation.text)
                            .append("×(").toString()
                    } else {
                        tvCalculation.text = StringBuilder(tvCalculation.text)
                            .append(")").toString()
                        tvResult.text = tvCalculation.text.calculate()
                    }
                }
                // comma: remove the comma because it is unused if bracket follows
                else {
                    tvCalculation.text = StringBuilder(
                        tvCalculation.text.subSequence(
                            0, tvCalculation.text.lastIndex
                        )
                    ).toString()
                    findViewById<Button>(R.id.buttonBrackets).performClick()
                    tvResult.text = tvCalculation.text.calculate()
                }
            }
        }

        // comma button: ,
        findViewById<Button>(R.id.buttonCom).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // if tvCalculation is empty and user types "," -> shows "0," (incomplete expression)
                if (tvCalculation.text.isEmpty()) {
                    tvCalculation.text = "0,"
                }
                // last Char is opening bracket '(' or an operator
                else if (tvCalculation.text[tvCalculation.text.lastIndex] == '('
                    || tvCalculation.text[tvCalculation.text.lastIndex].isOperator()
                ) {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("0,").toString()
                }
                // if the last character is a digit:
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit()) {
                    // if the most recent number already contains a comma, notify user about
                    // invalid expression, else append ','
                    if (tvCalculation.text.numberHasComma()) {
                        Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                    } else {
                        tvCalculation.text = StringBuilder(tvCalculation.text)
                            .append(",").toString()
                    }
                }
                // last Char is a closing bracket
                else if (tvCalculation.text[tvCalculation.text.lastIndex] == ')') {
                    tvCalculation.text = StringBuilder(tvCalculation.text)
                        .append("×0,").toString()
                }
                // if last Char is a comma: do not add the next comma (else expression invalid)

            }
        }

        // power operator button: ^
        // -> expression is incomplete right after appending an operator so we don't update tvResult
        findViewById<Button>(R.id.buttonPow).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // '^' cannot be the first Char of an expression or come after '(', notify the user with Toast
                if (tvCalculation.text.isEmpty() || (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '(')) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                // and notify the user about invalid expression
                else if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '-' ||
                            tvCalculation.text[tvCalculation.text.lastIndex] == '+')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the last Char is an operator or a ',', replace it with '^('
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isOperator() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ',') {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("^(")
                }
                // if the last Char is a digit, ')' or a '%', just append with '^('
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("^(")
                }

            }
        }

        // percentage button: %
        // todo update tvResult
        // todo 5%9
        // todo (50%) bracket replaces the operator
        findViewById<Button>(R.id.buttonPrc).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // '%' cannot be the first Char of an expression or come after '(', notify the user with Toast
                if (tvCalculation.text.isEmpty() || (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '(')) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                // and notify the user about invalid expression
                else if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '-' ||
                            tvCalculation.text[tvCalculation.text.lastIndex] == '+')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the last Char is an operator, replace it with '%'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isOperator()) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("%")
                }
                // if the last Char is a digit or a ')', just append with '%'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')'
                ) {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("%")
                }
                // if last Char is a comma, remove the comma
                else {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("%")
                }
            }
            tvResult.text = tvCalculation.text.calculate()
        }

        // division operator button: ÷
        // -> expression is incomplete right after appending an operator so we don't update tvResult
        findViewById<Button>(R.id.buttonDiv).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // '÷' cannot be the first Char of an expression or come after '(', notify the user with Toast
                if (tvCalculation.text.isEmpty() || (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '(')) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                // and notify the user about invalid expression
                else if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '-' ||
                            tvCalculation.text[tvCalculation.text.lastIndex] == '+')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the last Char is an operator, replace it with '÷'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isOperator()) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("÷")
                }
                // if the last Char is a digit, ')' or a '%', just append with '÷'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("÷")
                }
                // if last Char is a comma, remove the comma
                else {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("÷")
                }
            }
        }

        // multiplication operator button: ×
        // -> expression is incomplete right after appending an operator so we don't update tvResult
        findViewById<Button>(R.id.buttonMul).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // '×' cannot be the first Char of an expression or come after '(', notify the user with Toast
                if (tvCalculation.text.isEmpty() || (tvCalculation.text.isNotEmpty() && tvCalculation.text[tvCalculation.text.lastIndex] == '(')) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                // and notify the user about invalid expression
                else if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '-' ||
                            tvCalculation.text[tvCalculation.text.lastIndex] == '+')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the last Char is an operator, replace it with '×'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isOperator()) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("×")
                }
                // if the last Char is a digit, ')' or a '%', just append with '×'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("×")
                }
                // if last Char is a comma, remove the comma
                else {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("×")
                }
            }
        }

        // addition operator button: +
        // -> expression is incomplete right after appending an operator so we don't update tvResult
        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // '+' cannot be the first Char of an expression, notify the user with Toast
                if (tvCalculation.text.isEmpty()) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
                // if the second last Char is a '(' with a '-' following, replace the last Char
                else if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '-')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("+")
                }
                // if the last Char is an operator, replace it with '+'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isOperator()) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("+")
                }
                // if the last Char is a digit, a ')','(' or '%', just append with '+'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '(' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%') {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("+")
                }
                // if last Char is a comma, remove the comma
                else {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("+")
                }
            }
        }

        // subtraction operator button: -
        // -> expression is incomplete right after appending an operator so we don't update tvResult
        findViewById<Button>(R.id.buttonSub).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                // if the second last Char is a '(' with a '+' following, replace the last Char
                if (tvCalculation.text.length >= 2 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(' &&
                    (tvCalculation.text[tvCalculation.text.lastIndex] == '+')
                ) {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("-")
                }
                // if '-' is the first Char or comes after another operator (not '-' or '%'), add a '(' before it
                else if (tvCalculation.text.isEmpty() || (tvCalculation.text.isNotEmpty() &&
                            tvCalculation.text[tvCalculation.text.lastIndex].isOperator()
                            && tvCalculation.text[tvCalculation.text.lastIndex] != '-')
                ) {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("(-")
                }
                // if the last Char is a digit, a ')', '(' or '%', just append with '-'
                else if (tvCalculation.text[tvCalculation.text.lastIndex].isDigit() ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == ')' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '(' ||
                    tvCalculation.text[tvCalculation.text.lastIndex] == '%'
                ) {
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("-")
                }
                // if last Char is a comma, remove the comma
                else {
                    tvCalculation.text =
                        tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                    tvCalculation.text = StringBuilder(tvCalculation.text).append("-")
                }
            }
        }

        // delete button: DEL
        findViewById<Button>(R.id.buttonDel).setOnClickListener {
            if(tvCalculation.text.isNotEmpty()) {
                tvCalculation.text = StringBuilder(
                    tvCalculation.text.subSequence(
                        0,
                        tvCalculation.text.lastIndex
                    )
                ).toString()
                tvResult.text = tvCalculation.text.calculate()
            }
        }

        // clear button: C
        findViewById<Button>(R.id.buttonC).setOnClickListener {
            tvCalculation.text = ""
            tvResult.text = ""
        }

        // equals button: =
        findViewById<Button>(R.id.buttonEq).setOnClickListener {
            if (tvCalculation.text.isNotEmpty()) {
                tvCalculation.text = tvCalculation.text.calculate()
                tvResult.text = ""
            }
        }
    //todo: always test if tvCalculation.text is empty before accessing any indices!
    }

    // TODO for pasting: test if valid expression regarding brackets and adjust expression
    //  if needed -> also test for "-()+" parts
    // calculates the result of the expression by formatting, tokenizing it and calling
    // the recursive ArrayList<CharSequence>.calculate() function
    private fun CharSequence.calculate(): CharSequence {
        //Toast.makeText(this@MainActivity, "e: ${this.formatExpression()}", Toast.LENGTH_SHORT) .show()
        val tokenList = this.formatExpression().tokenList()
        // if ArrayList<CharSequence>.calculate() produces an ArithmeticException, set resultList as empty
        val resultList = try {
            tokenList.calculate(true)
        } catch(e: ArithmeticException) {
            ArrayList()
        }
        return if(resultList.isEmpty()) {
            ""
        } else {
            //Toast.makeText(this@MainActivity, "r: ${resultList[0]}", Toast.LENGTH_SHORT) .show()

            val result = resultList[0]
            tokenList.clear()
            // if last expression is percentage
            if(result[result.lastIndex] == '%') {
                return "${result.toNumber()}".formatNumber()
            }
            return result.formatNumber()
        }

    }

    // calculates result of formatted and tokenized expression  recursively
    private fun ArrayList<CharSequence>.calculate(firstCall: Boolean): ArrayList<CharSequence> {
        var tokenList = this

        if (tokenList.isEmpty()) {
            //Toast.makeText(this@MainActivity, "tokenList is empty", Toast.LENGTH_SHORT).show()
            return ArrayList()
        }
        // recursion end -> last element as result
        else if (tokenList.size == 1) {
            return tokenList
        }
        // merge tokens except on the initial recursive call, because expression has not been altered since tokenization
        if(!firstCall) {
            tokenList = tokenList.mergePercentages().mergePlusMinus()
        }

        // resolve the brackets
        if (tokenList.contains("(")) {
            var startIndex = 0
            for (index in tokenList.indices) {
                if (tokenList[index] == "(") {
                    startIndex = index
                    break
                }
            }
            // calculates index of the closing bracket that corresponds to the opening bracket at startIndex
            var endIndex = tokenList.lastIndex
            var bracketCounter = -1
            for (index in (startIndex + 1) .. tokenList.lastIndex) {
                if (tokenList[index] == "(") {
                    bracketCounter--
                }
                else if(tokenList[index] == ")") {
                    bracketCounter++
                }
                if(bracketCounter == 0) {
                    endIndex = index
                    break
                }
            }

            val leftExpression: ArrayList<CharSequence> =
                if (startIndex > 0) {
                    tokenList.subListExtension(0, startIndex)
                } else { ArrayList() }

            val rightExpression =
                if (endIndex < tokenList.lastIndex) {
                    tokenList.subList(endIndex + 1)
                } else { ArrayList() }

            // expression parts "()" should already never occur!
            assert(startIndex + 1 != endIndex)

            //Toast.makeText(this@MainActivity, "calculate: ${startIndex}", Toast.LENGTH_SHORT).show()

            //Log.d("startIndex", "${startIndex}")
            //Log.d("endIndex", "${endIndex}")

            val innerExpression = (tokenList.subListExtension(startIndex + 1, endIndex) as ArrayList)
            // Toast.makeText(this@MainActivity, "inner: ${innerExpression}", Toast.LENGTH_SHORT).show()

            val resultList = ArrayList<CharSequence>().apply {
                // no brackets here, but expression ends with an operator likely
                if(leftExpression.isNotEmpty()) { // todo
                    addAll(leftExpression)
                }
                addAll(innerExpression.calculate(false)) // more brackets may be there
                // no brackets here, but expression ends with an operator likely
                if(rightExpression.isNotEmpty()) {
                    addAll(rightExpression)
                }
            }

            Log.d("leftExpression", leftExpression.joinToString { ", " })
            Log.d("innerExpression", innerExpression.joinToString { ", " })
            Log.d("rightExpression", rightExpression.joinToString { ", " })


            //rightExpression.clear()
            //innerExpression.clear()
            //leftExpression.clear()

            // if resultList still contains brackets, keep resolving the brackets
            if(resultList.contains("(")) {
                return resultList.calculate(false)
            }
            // no brackets, so directly continue with operators
            else {
                tokenList = resultList.mergePercentages().mergePlusMinus()
            }
        }
        // operations, no more brackets (unary operators already calculated with retokenize() function)

        // percentage has the highest precedence as a unary operator
        // (together with '+' and '-', which we already considered)

        // multiplication, division exponentiation: same precedence (from left to right)
        val newTokenList: ArrayList<CharSequence> = tokenList
        while(tokenList.contains("×") || tokenList.contains("÷") || tokenList.contains("^")) {
            tokenList = newTokenList

            // find index of next operand
            val operatorIndex = tokenList.indexOfFirst { it == "×"
                    || it == "÷" || it == "^" }

            // assumes that each operator is surrounded by numbers left and right
            val operand1 = tokenList.elementAt(operatorIndex - 1).toNumber()
            val operand2 = tokenList.elementAt(operatorIndex + 1).toNumber()

            val result = if(tokenList.elementAt(operatorIndex) == "×") {
                (operand1).mul(operand2)
            } else if(tokenList.elementAt(operatorIndex) == "÷"){
                // may throw exception, which is handled by callee
                (operand1).div(operand2)
            } else {
                (operand1).power(operand2)
            }

            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.add(operatorIndex - 1, "$result")
        }
        tokenList = newTokenList
        // addition and subtraction: same precedence
        while(newTokenList.contains("+") || newTokenList.contains("-")) {
            // find index of next operand
            val operatorIndex = tokenList.indexOfFirst { it == "+" || it == "-" }

            // assumes that each operator is surrounded by numbers left and right
            val operand1 = tokenList.elementAt(operatorIndex - 1).toNumber()
            val operand2 = tokenList.elementAt(operatorIndex + 1).toNumber()

            val result = if(tokenList.elementAt(operatorIndex) == "+") {
                (operand1).add(operand2)
            } else {
                (operand1).sub(operand2)
            }

            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.removeAt(operatorIndex - 1)
            newTokenList.add(operatorIndex - 1, "$result")
        }

        //Toast.makeText(this@MainActivity, "returns: ${tokenList.forEach{ print("$it, ") } }", Toast.LENGTH_SHORT).show()
        return newTokenList
    }

    // returns true if CharSequence is a negative/positive integer/decimal, else false
// caution: does not specify how 'E' and ',' or '.' can be positioned in the CharSequence
// -> e.g. "4,E4" would return true todo
    private fun CharSequence.isNumeric(): Boolean {
        // CharSequence.none { ... } returns true if no Chars match the given predicate
        var commas = 0
        for(i in this.indices) {
            println(this) //
            // first Char has to be a digit, '+' or '-'
            if(i == 0 && (this[i] == '-' || this[i] == '+' || this[i].isDigit())) {
                continue
            }
            // count comma
            else if((this[i] == ',' || this[i] == '.') && commas == 0 && i != 0 && i != this.lastIndex
                && this[i - 1].isDigit() && this[i + 1].isDigit()) {
                commas++
                continue
            }
            // '%', 'E', and '-' following 'E' are allowed
            else if((i == this.lastIndex && this[i] == '%') ||
                (i != this.lastIndex && i !=0 && this[i] == 'E') ||
                (i != this.lastIndex && i !=0 && this[i] == '-' && this[i - 1] == 'E'))   {
                continue
            }
            // Char is not a digit (or 'E' or 'E' + '-'), or it is a second comma -> false
            else if(!this[i].isDigit()) {
                //println("2nd false")
                //println("${this[i]}  ${this[i + 1]}")
                return false
            }
        }
        return true
    }


    // true if Char is an operator
    private fun Char.isOperator(): Boolean {
        return this == '+' || this == '-' || this == '×'
                || this == '÷' || this == '^'
    }

    // picks an opening or closing bracket based on the typed text in the calculation TextView
    // to append to the expression later if the user presses the bracket button
    private fun CharSequence.bracketPicker(): CharSequence {
        var count = 0
        for (char in this) {
            if (char == '(') {
                count--
            }
            if (char == ')') {
                count++
            }
        }
        if (count == 0 || count < 0 && this[length - 1] == '(') {
            return "("
        }
        return ")"
    }

    // helper function for ',' or '.': ensures a number has a maximum of one ',' or '.'
    private fun CharSequence.numberHasComma(): Boolean {
        // c remains null if the sequence contains digits only
        var c: Char? = null
        // checks all Chars starting from the end until the first Char that is not a digit
        for (i in this.lastIndex downTo 0) {
            if (this[i].isDigit()) {
                continue
            } else {
                // set first non-digit Char
                c = this[i]
                break
            }
        }
        return c == ',' || c == '.'
    }

    // if needed, removes incomplete expression Chars at the end and adds missing closing brackets
    // to prepare for calculate() function
    private fun CharSequence.formatExpression(): CharSequence {
        if (this.isEmpty()) {
            return this
        }
        var exprToFormat = this
        // remove last Char if it doesn't complete the expression
        while(exprToFormat.isNotEmpty() && !(exprToFormat[exprToFormat.lastIndex] == ')' || exprToFormat[exprToFormat.lastIndex].isDigit() ||
                    exprToFormat[exprToFormat.lastIndex] == '%')) {
            exprToFormat = exprToFormat.subSequence(0, exprToFormat.lastIndex)
        }
        // add the missing closing brackets if needed
        while (exprToFormat.contains('(') && exprToFormat.bracketPicker() == ")") {
            exprToFormat = exprToFormat.append(")")
        }
        return exprToFormat
    }

    // helper function to convert CharSequence into Int variables
// CAUTION: throws IllegalArgumentException if the CharSequence is not numeric
    private fun CharSequence.toNumber(): Number {
        var seq = this
        var number: Number
        var isPercentage = false
        if(!seq.isNumeric()) {
            throw IllegalArgumentException("CharSequence.toNumber() cannot be called on a non-numeric CharSequence")
        }
        // replace ',' with '.'
        if(seq.contains(',')) {
            seq = seq.replace(Regex(","), ".")
        }
        // if number is a percentage, remember it for later and remove the percentage
        if(seq[seq.lastIndex] == '%') {
            isPercentage = true
            seq = seq.subSequence(0, seq.lastIndex)
        }
        // returns Double
        if(seq.toString().toDoubleOrNull() != null) {
            number = seq.toString().toDoubleOrNull() as Number
        } else {
            // will never be reached because seq.isNumeric() must be true (see above)
            return Double.NaN
        }
        if(isPercentage) {
            // todo avoid rounding
            number = number.toDouble() / 100
        }
        return number
    }

    // converts a CharSequence expression into a list of CharSequence tokens
    // assumes the CharSequence is already formatted
    private fun CharSequence.tokenList(): ArrayList<CharSequence> {
        val tokenList = ArrayList<CharSequence>()
        var number = ""

        for (index in this.indices) {
            try {
                // append current number, '+','-' or '%' sign
                if ((index == 0 && (this[index] == '-' || this[index] == '+') && this[1].isDigit() ||
                    index > 0 && this[index - 1] == '(' && (this[index] == '-' || this[index] == '+')
                    && this[index + 1].isDigit())
                    || this[index].isDigit() || this[index] == ',' || this[index] == '.' ||
                    (index > 0 && this[index - 1].isDigit() && this[index] == '%')) {
                    number += this[index]
                }
                // add number to tokenList
                else {
                    if (number.isNotEmpty()) {
                        tokenList.add(number)
                        number = ""
                    }
                    // add operator or bracket to tokenList
                    tokenList.add(this[index].toString())
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity,
                    ".tokenList() called on not tokenized expression", Toast.LENGTH_LONG).show()
                return ArrayList()
            }
        }
        if (number.isNotEmpty()) {
            tokenList.add(number)
        }
        return tokenList
    }

    // custom function for ArrayList<CharSequence> class
    // -> returns a subList that contains all elements starting from the specified index
    private fun ArrayList<CharSequence>.subList(fromIndex: Int): ArrayList<CharSequence> {
        // if index is out of range, throws IllegalArgumentException
        if (fromIndex !in this.indices) {
            throw IllegalArgumentException(
                "fromIndex in ArrayList<CharSequence>.subList(fromIndex: Int) is out of bounds")
        }
        val subList = ArrayList<CharSequence>()
        for(i in fromIndex..this.lastIndex) {
            subList.add(this[i])
        }
        return subList
    }

    // custon extension function for ArrayList<CharSequence> class
    // -> returns a subList that contains all elements starting from the startIndex until the endIndex (exclusive)
    private fun ArrayList<CharSequence>.subListExtension(startIndex: Int, endIndex: Int): ArrayList<CharSequence> {
        // if index is out of range, throws IllegalArgumentException
        if (startIndex !in this.indices || endIndex !in this.indices) {
            throw IllegalArgumentException(
                "fromIndex in ArrayList<CharSequence>.subList(fromIndex: Int) is out of bounds")
        }
        val subList = ArrayList<CharSequence>()
        for(i in startIndex..<endIndex) {
            subList.add(this[i])
        }
        return subList
    }

    // custom subSequence() function
// -> returns a subSequence that contains all Chars starting from the specified index
    private fun CharSequence.subSequence(fromIndex: Int): CharSequence {
        // if index is out of range, throws IllegalArgumentException
        if (fromIndex !in this.indices) {
            throw IllegalArgumentException(
                "fromIndex in ArrayList<CharSequence>.subList(fromIndex: Int) is out of bounds")
        }
        val subSequence = StringBuilder()
        for(i in fromIndex..this.lastIndex) {
            subSequence.append(this[i])
        }
        return subSequence
    }

    // multiplication
    private fun Number.mul(other: Number): Number {
        return (this as Double) * (other as Double)
    }

    // division
    private fun Number.div(other: Number): Number {
        if(other.toDouble() == 0.0) {
            Toast.makeText(this@MainActivity, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
            throw ArithmeticException("Cannot divide by zero in Number.div(other: Number)")
        }
        return (this as Double) / (other as Double)
    }

    // todo special case of the pow() function: if base is < 0 -> returns NaN
    // exponentiation
    private fun Number.power(other: Number): Number {
        /*if((this as Double) < 0 && other.) { // todo decimal digit of other is 0
            //todo cast other to Int
            // todo make calculation or this < 0 correctly
            return (this as Double).pow(other as Int)
        }*/
        return (this as Double).pow(other as Double)
    }

    // addition
    private fun Number.add(other: Number): Number {
        return (this as Double) + (other as Double)
    }

    // subtraction
    private fun Number.sub(other: Number): Number {
        return (this as Double) - (other as Double)
    }

    // percentage
    private fun Number.percent(): Number {
        // remove last Char '%'
        val number =  "$this".subSequence(0, "$this".lastIndex)
        return number.toNumber().div(100)
    }

    // merges tokens like "+""+n", "+""-n", "-""n" and "-""-n", which can occur after
    // recursively resolving brackets in calculate()
    private fun ArrayList<CharSequence>.mergePlusMinus(): ArrayList<CharSequence> {
        val tokenList = ArrayList<CharSequence>()
        var tokensMultiplication = ArrayList<CharSequence>()
        var mergedValue: CharSequence
        for(index in this.indices) {
            if(index == 1 && this[0].isPlusMinus() && this[1].isNumeric() ||
                index >= 2 && this[index - 2] == "(" && this[index - 1].isPlusMinus() && this[index].isNumeric()) {
                tokensMultiplication.add("(")
                tokensMultiplication.add("${this[index - 1]}1")
                tokensMultiplication.add(")")
                tokensMultiplication.add("×")
                tokensMultiplication.add(this[index])
                mergedValue = tokensMultiplication.calculate(true)[0]
                tokensMultiplication.clear()
                tokenList.removeAt(index - 1)
                tokenList.add(mergedValue)

            } else {
                tokenList.add(this[index])
            }
        }
        return tokenList
    }

    // merges percentage Chars with a number after bracket resolution in calculate()
    private fun ArrayList<CharSequence>.mergePercentages(): ArrayList<CharSequence> {
        val tokenList: ArrayList<CharSequence> = ArrayList()
        for(i in this.indices) {
            // merge numbers with '%'
            if(i > 0 && this[i] == "%" && this[i - 1].isNumeric()) {
                tokenList.removeAt(i - 1)
                tokenList.add("${this[i - 1]}${this[i]}")
            }
            else {
                tokenList.add(this[i])
            }
        }
        return tokenList
    }

    // helper function for mergePlusMinus to determine the token type
    // -> returns true if the CharSequence is "+" or "-"
    private fun CharSequence.isPlusMinus(): Boolean {
        return this == "+" || this == "-"
    }

    // formats the final result by removing unnecessary '0's at the end after the decimal point
    // uniformly uses ',' in Double types
    private fun CharSequence.formatNumber(): CharSequence {
        if(!this.isNumeric()) {
            throw IllegalArgumentException("CharSequence.formatNumber(): this.isNumeric() has to return true")
        }
        var number = this
        if(number.contains(',') || number.contains('.')) {
            // replace '.' with ','
            number = number.replace(Regex("\\."), ",")
            val decimalPoint = number.indexOf(",")
            val postDecimalPoint = number.subSequence(decimalPoint + 1)
            // format to integer if all digits after the decimal point are '0'
            if(postDecimalPoint.all { char -> char == '0' }) {
                number = number.subSequence(0,decimalPoint)
            }
            // remove all unnecessary '0' after the decimal point at the end
            else {
                var index = postDecimalPoint.lastIndex
                // remove last Char in number until the last digit after the decimal point is not '0'
                while(index >= 0 && postDecimalPoint[index] == '0') { // todo does nothing -> fix
                    index--
                    number = number.subSequence(0, number.lastIndex)
                }
            }
            // round to a maximum of 10 decimal places
            if(postDecimalPoint.length > 10) {
                number = String.format("%.10f", number.toNumber())
            }
        }
        // number is in Int format or in Double format with a non-zero decimal-value
        return number
    }

    // todo better use StringBuilter for more memory efficiency
    // helper function to append a CharSequence to another CharSequence
    private fun CharSequence.append(other: CharSequence): CharSequence {
        return "${this}${other}"
    }
}




