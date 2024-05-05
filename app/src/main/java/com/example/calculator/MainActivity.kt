package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // defines the maximum amount of Chars in the calculation TextView
        val maxAmountOfChars = 16

        val tvCalculation: TextView = findViewById(R.id.tvCalculation)
        val tvResult: TextView = findViewById(R.id.tvResult)

        // general function to set onClickListeners for the digit buttons 1 - 9
        fun setListenerDigitsNonZero(button: Button) {
            button.setOnClickListener {
                if (tvCalculation.text.length < maxAmountOfChars) {

                    val lastChar = if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                    val calculationText = when {
                        lastChar == null -> button.text
                        lastChar in setOf(')', '%') -> "${tvCalculation.text}×${button.text}"
                        lastChar == '0' && (tvCalculation.text.length == 1 || !(tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit()
                                || tvCalculation.text.numberHasCommaOrDot())) ->
                            tvCalculation.text.dropLast(1).append(button.text)
                        lastChar == 'E' -> tvCalculation.text.dropLast(1)
                        else -> "${tvCalculation.text}${button.text}"
                    }
                    if(lastChar == 'E') {
                        Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                    }
                    tvCalculation.text = calculationText
                    val result = calculationText.calculate()
                    // check whether result is out of range or has a division by zero
                    if(result == "outOfRange" || result == "divisionByZero") {
                        tvResult.text = ""
                    } else {
                        tvResult.text = result
                    }
                }
            }
        }

        // set all onClickListeners for the digit buttons 1 - 9
        arrayOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9).forEach {
                setListenerDigitsNonZero(findViewById(it)) }

        // zero button
        findViewById<Button>(R.id.button0).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                val lastChar = if(tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                val calculationText: CharSequence = when {
                    lastChar == null -> "0"
                    lastChar in setOf(')', '%') -> "${tvCalculation.text}×0" // todo is append() more efficient?
                    // do nothing if a new zero is added with no number directly before it
                    (tvCalculation.text == "0") ||
                            (lastChar == '0' && tvCalculation.text.length > 1
                                    && !((tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit() ||
                                    tvCalculation.text.numberHasCommaOrDot()))) -> tvCalculation.text
                    lastChar == 'E' -> tvCalculation.text.dropLast(1)
                    else -> "${tvCalculation.text}0"
                }
                if(lastChar == 'E') {
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                }
                tvCalculation.text = calculationText
                val resultText = calculationText.calculate()
                // check whether result is out of range or has a division by zero
                if(resultText == "outOfRange" || resultText == "divisionByZero") {
                    tvResult.text = ""
                } else {
                    tvResult.text = resultText
                }
            }
        }

        // brackets button: ()
        findViewById<Button>(R.id.buttonBrackets).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {

                val lastChar = if(tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                tvCalculation.text = when {
                    lastChar == null || lastChar == '(' || lastChar.isOperator() -> "${tvCalculation.text}("
                    lastChar.isDigit() || lastChar == '%' || lastChar == ')' ->
                        if(tvCalculation.text.bracketPicker() == "(") "${tvCalculation.text}×(" else "${tvCalculation.text})"
                    lastChar == 'E' -> tvCalculation.text.dropLast(1)
                    // comma: remove the comma because it is unused if bracket follows
                    else -> if(tvCalculation.text.bracketPicker() == "(")
                        "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)}×("
                    else "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)})"
                }
                if(lastChar == 'E') {
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // comma button: ,
        findViewById<Button>(R.id.buttonCom).setOnClickListener {
            if (tvCalculation.text.length < maxAmountOfChars) {
                val lastChar = if(tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                if(!tvCalculation.text.numberHasCommaOrDot() || lastChar == ',') {
                    val (calculationText, resultText) = when {
                        // if tvCalculation is empty and user types "," -> shows "0," (incomplete expression)
                        lastChar == null -> "0," to "0"
                        // last Char is opening bracket '(' or an operator
                        lastChar == '(' || lastChar.isOperator() -> "${tvCalculation.text}0," to "${tvCalculation.text}0".calculate()
                        lastChar.isDigit() -> "${tvCalculation.text}," to tvResult.text
                        lastChar == ')' || lastChar == '%' -> "${tvCalculation.text}×0," to "${tvCalculation.text}×0".calculate()
                        lastChar == 'E' -> tvCalculation.text.dropLast(1) to "${tvCalculation.text.dropLast(1).calculate()}"
                        // if last Char is a comma do not add another one, but do not notify the user
                        else -> tvCalculation.text to tvResult.text
                    }
                    if(lastChar == 'E') {
                        Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                    }
                    // update the TextViews
                    tvCalculation.text = calculationText
                    // check whether result is out of range or has a division by zero
                    if(resultText == "outOfRange" || resultText == "divisionByZero") {
                        tvResult.text = ""
                    } else {
                        tvResult.text = resultText
                    }
                } else {
                    // if the last number already contains a comma and it is not the last Char, do not
                    // alter any text and notify the user
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // sets the onClickListeners of the buttons mul, div, pow, prc
        fun setListenersMulDivPowPrc(button: Button) {
            button.setOnClickListener {
                if (tvCalculation.text.length < maxAmountOfChars) {

                    val operator = if(button.text.length > 1) "^(" else button.text  // pow is a special case
                    val lastChar = if(tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                    // becomesInvalid -> keeps track of whether the expression becomes invalid after click action
                    val (calculationText, becomesInvalid) = when {
                        // invalid: operator cannot be the first Char of an expression or come after '('
                        lastChar == null || lastChar == '(' -> tvCalculation.text to true
                        // do nothing if lastChar is already the pressed button, can't have double operators
                        lastChar == operator[0] -> tvCalculation.text to false
                        // invalid: if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                        (lastChar == '+' || lastChar == '-') && tvCalculation.text.length > 1 &&
                                tvCalculation.text[tvCalculation.text.lastIndex - 1] == '('
                            -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex) to true
                        // if the last Char is an operator or a ',', replace it with '[op]' ('[op](' for ^)
                        lastChar.isOperator() || lastChar == ','
                            -> "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)}${operator}" to false
                        lastChar == 'E' -> tvCalculation.text.dropLast(1) to true
                        // if the last Char is a digit, ')' or a '%', just append with '[op]('
                        else ->  "${tvCalculation.text}${operator}" to false
                    }
                    // notify the user about invalid expression
                    if(becomesInvalid) { Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show() }
                    tvCalculation.text = calculationText
                    // only if adding "%" the result of the expression changes
                    if(operator == "%") {
                        tvResult.text = tvCalculation.text.calculate()
                    }
                }
            }
        }
        // set the onClickListeners of the buttons mul, div, pow, prc
        arrayOf(R.id.buttonMul, R.id.buttonDiv, R.id.buttonPow, R.id.buttonPrc).forEach { setListenersMulDivPowPrc(findViewById(it)) }

        // function to set the listeners for the add and sub buttons
        fun setListenersAddSub(button: Button) {
            val thisOperator = button.text
            val otherOperator = if(button.text == "+") "-" else "+"

            button.setOnClickListener {
                if (tvCalculation.text.length < maxAmountOfChars) {

                    val lastChar = if(tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                    tvCalculation.text = when {
                        // if [thisOperator] is the first Char or comes after another operator (not [thisOperator] or '%'), add a '(' before it
                        (lastChar == null || (thisOperator == "-" && lastChar.isOperator()
                                && lastChar != thisOperator[0])) -> tvCalculation.text.append("($thisOperator")

                        // if the second last Char is a '(' with a [otherOperator] following, replace the last Char
                        (tvCalculation.text.length >= 2) && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(') &&
                                (tvCalculation.text.last() == otherOperator[0])
                                        -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex).append(thisOperator)
                        // if the last Char is a digit, a ')', '(' or '%', just append with [thisOperator]
                        lastChar.isDigit() || lastChar == ')' || lastChar == '(' || lastChar == '%'
                            -> tvCalculation.text.append(thisOperator)
                        lastChar == 'E' -> tvCalculation.text.dropLast(1)
                        // if last Char is a comma, remove the comma, or for plus: last operator gets replaced
                        else -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex).append(thisOperator)
                    }
                    if(lastChar == 'E') {
                        Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        // setting listeners of add and sub
        setListenersAddSub(findViewById(R.id.buttonAdd))
        setListenersAddSub(findViewById(R.id.buttonSub))

        // delete button: DEL
        findViewById<Button>(R.id.buttonDel).setOnClickListener {
            if(tvCalculation.text.isNotEmpty()) {
                // if the last Chars are '^' and '(' following, remove both, else remove only one last Char
                tvCalculation.text = when {
                    tvCalculation.text.length > 1 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '^'
                            && tvCalculation.text.last() == '('
                    -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex - 1)
                    // else remove only the last Char
                    else -> tvCalculation.text.dropLast(1)
                }
                // invalid scientific number in calculation view
                val invalidExpression = if(tvCalculation.text.isNotEmpty()) {
                    tvCalculation.text.last() == 'E' || tvCalculation.text.length > 1
                            && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == 'E')
                            && tvCalculation.text.last() == '-' || tvCalculation.text.last() == '+'
                } else {
                    false
                }
                val result = if(tvCalculation.text.isNotEmpty() && !invalidExpression) tvCalculation.text.calculate() else ""

                // check whether result is out of range or has a division by zero
                if(result == "outOfRange" || result == "divisionByZero") {
                    tvResult.text = ""
                } else {
                    tvResult.text = result
                }
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
                val result: CharSequence
                // invalid scientific number in calculation view
                val invalidExpression =  tvCalculation.text.last() == 'E' || tvCalculation.text.length > 1
                        && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == 'E')
                        && tvCalculation.text.last() == '-' || tvCalculation.text.last() == '+'
                if(invalidExpression) {
                    result = ""
                    Toast.makeText(this, "InvalidExpression", Toast.LENGTH_SHORT).show()
                } else {
                    result = tvCalculation.text.calculate()
                }

                // if the result is out of range, empty everything and notify the user
                when(result) {
                    "outOfRange" -> apply { tvCalculation.text = ""
                        Toast.makeText(this, "Cannot calculate outside of the allowed range.", Toast.LENGTH_SHORT).show() }
                    "divisionByZero" ->  Toast.makeText(this, "Cannot divide by zero.", Toast.LENGTH_SHORT).show()
                    else -> tvCalculation.text = result
                }
                tvResult.text = ""
            }
        }
    }


    // calculates the result of the expression by formatting, tokenizing it and calling
    // the recursive ArrayList<CharSequence>.calculate() function

    // CAUTION: Can return a CharSequence representing a numerical result
    // OR the label "outOfRange", which indicates that the result of the calculation
    // (or a part of it) is too large or too small
    // -> "outOfRange" results from a NumberFormatException thrown in toNumber() or any of the
    // operator functions
    private fun CharSequence.calculate(): CharSequence {
        val tokenList = this.tokenList()
        val resultList: ArrayList<CharSequence>
        try {
            resultList = tokenList.calculate(true)
        } catch(e: ArithmeticException) {
            // returns a label so caller function can decide whether to notify the user or not
            return "divisionByZero"
        } catch(e: NumberFormatException) {
            // returns a label so caller function can decide whether to notify the user or not
            return "outOfRange"
        }
        return if(resultList.isEmpty()) {
            ""
        } else {
            // replace the dot with a comma if available
            var result = resultList[0]
            resultList.clear()
            // if last expression is percentage resolve percentages via toNumber
            if(result[result.lastIndex] == '%') {
                result = result.toNumber().toString()
            }
            return result.formatNumber().replace(Regex("\\."), ",")
        }
    }

    // calculates result of formatted and tokenized expression  recursively
    @Throws(NumberFormatException::class, ArithmeticException:: class)
    private fun ArrayList<CharSequence>.calculate(firstCall: Boolean): ArrayList<CharSequence> {
        var tokenList = this

        if (tokenList.isEmpty()) {
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
            val leftExpression: ArrayList<CharSequence> = tokenList.subListExtension(0, startIndex)

            val rightExpression =
                if (endIndex < tokenList.lastIndex) {
                    tokenList.subList(endIndex + 1)
                } else { ArrayList() }

            val innerExpression = tokenList.subListExtension(startIndex + 1, endIndex)
            // Toast.makeText(this@MainActivity, "inner: ${innerExpression}", Toast.LENGTH_SHORT).show()

            val resultList = ArrayList<CharSequence>().apply {
                // no brackets here, but expression ends with an operator likely
                if(leftExpression.isNotEmpty()) {
                    addAll(leftExpression)
                }
                addAll(innerExpression.calculate(false)) // more brackets may be there
                // no brackets here, but expression ends with an operator likely
                if(rightExpression.isNotEmpty()) {
                    addAll(rightExpression)
                }
            }
            rightExpression.clear()
            innerExpression.clear()
            leftExpression.clear()

            // if resultList still contains brackets (that were not all nested), keep resolving the brackets
            if(resultList.contains("(")) {
                return resultList.calculate(false)
            }
            // no brackets, so directly continue with operators
            else {
                tokenList = resultList.mergePercentages().mergePlusMinus()
            }
        }
        // operations, no more brackets
        // percentage has the highest precedence as a unary operator
        // (together with '+' and '-', which we already considered)

        val newTokenList: ArrayList<CharSequence> = tokenList
        while(tokenList.contains("×") || tokenList.contains("÷") || tokenList.contains("^")
            || newTokenList.contains("+") || newTokenList.contains("-")) {

            tokenList = newTokenList
            val operatorIndex: Int = if(!(tokenList.contains("-") || tokenList.contains("+"))) {
                // multiplication, division exponentiation: same precedence (from left to right)
                if(tokenList.contains("^")) { //"^" has highest precedence
                    tokenList.indexOfFirst{ it == "^" }
                } else {tokenList.indexOfFirst { it == "×"
                        || it == "÷" }
                }
            }
            // add and sub (binary) have lower precedence
            else {
                tokenList.indexOfFirst { it == "+" || it == "-" }
            }
            // assumes that each operator is surrounded by numbers left and right
            val operand1 = tokenList.elementAt(operatorIndex - 1).toNumber()
            val operand2 = tokenList.elementAt(operatorIndex + 1).toNumber()

            val result = when(tokenList.elementAt(operatorIndex)) {
                "^" -> (operand1).power(operand2)
                "×" -> (operand1).mul(operand2)
                "÷" -> // may throw exception, which is handled by callee
                    (operand1).div(operand2)
                "+" -> (operand1).add(operand2)
                else -> (operand1).sub(operand2)
            }
            // remove both operands and operator and insert result
            repeat(3) { newTokenList.removeAt(operatorIndex - 1) }
            newTokenList.add(operatorIndex - 1, "$result")
        }
        return newTokenList
    }

    // returns true if CharSequence is a negative/positive integer/decimal, else false
    // caution: does not specify how 'E' and ',' or '.' can be positioned in the CharSequence
    // -> e.g. "4,E4" would return true todo
    private fun CharSequence.isNumeric(): Boolean {
        var commas = 0
        for(i in this.indices) {
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
            else if((i == lastIndex && this[i] == '%') ||
                (i != lastIndex && i !=0 && this[i] == 'E') ||
                (i != lastIndex && i !=0 && this[i] == '-' && this[i - 1] == 'E'))   {
                continue
            }
            // Char is not a digit (or 'E' or 'E' + '-'), or it is a second comma -> false
            else if(!this[i].isDigit()) {
                return false
            }
        }
        return true
    }


    // true if Char is an operator
    private fun Char.isOperator(): Boolean {
        return this == '+' || this == '-' || this == '×' || this == '÷' || this == '^'
    }

    // picks an opening or closing bracket based on the typed text in the calculation TextView
    // to append to the expression later if the user presses the bracket button
    private fun CharSequence.bracketPicker(): CharSequence {
        var count = 0
        forEach { char -> if(char == '(') count-- }
        forEach { char -> if(char == ')') count++ }
        if (count == 0 || count < 0 && this[lastIndex] == '(') {
            return "("
        }
        return ")"
    }

    // helper function for ',' or '.': ensures a number has a maximum of one ',' or '.'
    private fun CharSequence.numberHasCommaOrDot(): Boolean {
        // char remains null if the sequence contains digits only
        var char: Char? = null
        // checks all Chars starting from the end until the first Char that is not a digit
        for (i in this.lastIndex downTo 0) {
            if (this[i].isDigit()) {
                continue
            } else {
                // set first non-digit Char
                char = this[i]
                break
            }
        }
        return char == ',' || char == '.'
    }

    // if needed, removes incomplete expression Chars at the end and adds missing closing brackets
    // to prepare for calculate() function
    private fun CharSequence.formatExpression(): CharSequence {
        if (this.isEmpty()) {
            return this
        }
        var exprToFormat = this
        // remove last Char if it doesn't complete the expression
        while(exprToFormat.isNotEmpty() && !(exprToFormat.last() == ')' || exprToFormat.last().isDigit() ||
                    exprToFormat.last() == '%')) {
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
    @Throws(NumberFormatException::class)
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
        if(seq.last() == '%') {
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
            val result = number.toDouble() / 100
            // throws exception if result is infinity (distinction between pos./neg. infinity)
            throwExceptionInfinity(result)
            number = result
        }
        return number
    }

    // converts a CharSequence expression into a list of CharSequence tokens
    @Throws(IllegalArgumentException::class)
    private fun CharSequence.tokenList(): ArrayList<CharSequence> {
        val oldList = this.formatExpression() // Only call on formatted expression
        val newList = ArrayList<CharSequence>()
        var number = ""

        for (index in oldList.indices) {
            // append current number, '+','-', 'E', ',' or '%' sign
            if (oldList[index].isDigit() || oldList[index] == ',' ||
                (index == 0 && (oldList[0] == '-' || oldList[0] == '+') && oldList[1].isDigit()) ||
                index > 0 && ((index + 1 in indices && (oldList[index - 1] == '(' || oldList[index - 1] == 'E')
                        && (oldList[index] == '-' || oldList[index] == '+') && oldList[index + 1].isDigit()) ||
                        (oldList[index - 1].isDigit() && (oldList[index] == '%' || oldList[index] == 'E')))) {
                number += oldList[index]
            }
            // add number to tokenList
            else {
                if (number.isNotEmpty()) {
                    if(number.last() == 'E') {
                        throw IllegalArgumentException("Invalid expression.")
                    }
                    newList.add(number)
                    number = ""
                }
                // add operator or bracket to tokenList
                newList.add(oldList[index].toString())
            }
        }
        if (number.isNotEmpty()) {
            newList.add(number)
        }
        return newList
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

    // custom extension function for ArrayList<CharSequence> class
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
    @Throws(NumberFormatException::class)
    private fun Number.mul(other: Number): Number {
        val result = (this as Double) * (other as Double)
        // throws exception if result is infinity
        throwExceptionInfinity(result)
        return result
    }

    // division
    @Throws(NumberFormatException::class, ArithmeticException::class)
    private fun Number.div(other: Number): Number {
        if(other.toDouble() == 0.0) {
            throw ArithmeticException("Cannot divide by zero in Number.div(other: Number)")
        }
        val result = (this as Double) / (other as Double)
        // throws exception if result is infinity
        throwExceptionInfinity(result)
        return result
    }

    // todo special case of the pow() function: if base is < 0 and exp is not an Int -> returns NaN
    // exponentiation
    @Throws(NumberFormatException::class)
    private fun Number.power(other: Number): Number {
        /*if((this as Double) < 0 && other.) { // todo decimal digit of other is 0
            //todo cast other to Int
            // todo make calculation or this < 0 correctly
            return (this as Double).pow(other as Int)
        }*/
        val result = (this as Double).pow(other as Double)
        // throws exception if result is Infinity
        throwExceptionInfinity(result)
        return result
    }

    // addition
    @Throws(NumberFormatException::class)
    private fun Number.add(other: Number): Number {
        val result = (this as Double) + (other as Double)
        // throws exception if result is infinity (distinction between pos./neg. infinity)
        throwExceptionInfinity(result)
        return result
    }

    // subtraction
    @Throws(NumberFormatException::class)
    private fun Number.sub(other: Number): Number {
        val result = (this as Double) - (other as Double)
        // throws exception if result is infinity (distinction between pos./neg. infinity)
        throwExceptionInfinity(result)
        return result
    }

    // merges tokens like "+""+n", "+""-n", "-""n" and "-""-n", which can occur after
    // recursively resolving brackets in the recursive calculate()
    //
    // CAUTION: relies on the fact that it is ONLY called by ArrayList<CharSequence>.calculate()
    // because then CharSequence.calculate() can catch the potential NumberFormatException
    @Throws(NumberFormatException::class)
    private fun ArrayList<CharSequence>.mergePlusMinus(): ArrayList<CharSequence> {
        val tokenList = ArrayList<CharSequence>()
        var mergedValue: CharSequence
        val unaryMinusSubFirst = (this[0] == "-" || this[0] == "+") && this[1].isNumeric()
        for(index in this.indices) {
            if(unaryMinusSubFirst || index >= 2 && this[index - 2] == "(" && (this[index - 1] == "-"
                        || this[index - 1] == "+") && this[index].isNumeric()) {
                // mul() can throw a NumberFormatException theoretically,
                // but practically never will because we only multiply by +1 or -1
                mergedValue = ("${this[index - 1]}1".toNumber() as Double * this[index].toNumber() as Double).toString()
                tokenList.removeAt(index - 1)
                tokenList.add(mergedValue)
            } else {
                tokenList.add(this[index])
            }
        }
        return tokenList
    } // todo combine both merge functions

    // merges percentage Chars with a number after bracket resolution in calculate()
    private fun ArrayList<CharSequence>.mergePercentages(): ArrayList<CharSequence> {
        val tokenList: ArrayList<CharSequence> = ArrayList()
        for(index in this.indices) {
            // merge numbers with '%'
            if(index > 0 && this[index] == "%" && this[index - 1].isNumeric()) {
                tokenList.removeAt(index - 1)
                tokenList.add("${this[index - 1]}${this[index]}")
            }
            else {
                tokenList.add(this[index])
            }
        }
        return tokenList
    }

    // formats the final result by removing unnecessary '0's  or at the end after the decimal point
    // -> returns in double- or integer-format if possible
    // Note:
    // conversion from dot-format to comma-format of doubles avoided to avoid unnecessary conversions
    // throughout recursive ArrayList<CharSequence>calculation() calls. Instead the conversion of
    // the final result to comma-format is executed in the caller CharSequence.calculate()
    @Throws(java.lang.IllegalArgumentException::class)
    private fun CharSequence.formatNumber(): CharSequence {
        if(!this.isNumeric()) {
            throw IllegalArgumentException("CharSequence.formatNumber(): this.isNumeric() has to return true")
        }
        var number = this
        if(number.numberHasCommaOrDot()) {
            // replace ',' with  '.' for uniformity of calculation
            number = number.replace(Regex(","), "\\.")
            val decimalPoint = number.indexOf(".")
            val postDecimalPoint = number.subSequence(decimalPoint + 1)
            // return as integer if all digits after the decimal point are '0'
            if(postDecimalPoint.all { char -> char == '0' }) {
                return number.subSequence(0, decimalPoint)
            }
            // is double: round to a maximum of 10 decimal places
            if(postDecimalPoint.length > 10) {
                number = String.format("%.10f", number.toNumber())
            }
            // if there are unnecessary '0' at the end after the decimal point, remove them
            while(number[number.lastIndex] == '0') {
                number = number.subSequence(0, number.lastIndex)
            }
        }
        // number is in Int format or in Double format with a non-zero decimal-value
        return number
    }

    // helper function to append a CharSequence to another CharSequence
    private fun CharSequence.append(other: CharSequence): CharSequence {
        return "${this}${other}"
    }
    // helper function throw NumberFormatException in case of infinity results
    @Throws(NumberFormatException::class)
    private fun throwExceptionInfinity(result: Double) {
        if(result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY){
            throw NumberFormatException()
        }
    }

}