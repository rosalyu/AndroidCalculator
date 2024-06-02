package com.example.calculator

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import java.util.Locale
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var tvCalculation: TextView
    private lateinit var tvResult: TextView
    private lateinit var buttonPanel: ConstraintLayout

    // setting a vibrator to create buttonColor vibrations when a button is pressed
    private var vibrator: Vibrator? = null
    private val vibrationDurationMilliSec = 50L
    // initialized in onConfigurationChanged()
    private lateinit var displayMetrics: DisplayMetrics

    private var buttonPanelHeightPortrait: Int? = null
    private var buttonPanelWidthLand: Int? = null

    private lateinit var sharedPreferences: SharedPreferences
    private var themeId: Int? = null
    // tracks if recreate is due to theme changes, in which case the configuration does not need to be adjusted
    private var themeChangedRecreated: Boolean? = null

    // defines the maximum amount of Chars in the calculation TextView
    private val maxCharAmount = 18

    // only runs once to set the default theme
    init {
        themeId = R.style.Theme_Default
        themeChangedRecreated = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "")

        sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

        // the first creation of the activity
        if(savedInstanceState == null) {
            vibrator = ContextCompat.getSystemService(applicationContext, Vibrator::class.java)
        } else {
            themeId = savedInstanceState.getInt("themeId")
            themeChangedRecreated = savedInstanceState.getBoolean("themeChangedRecreated")
        }

        //Log.d("themeIdIsSaved", themeIdIsSaved.toString())
        //Log.d("themeId onCreate", themeId.toString())


        //theme.applyStyle(themeId!!, true)
        setTheme(themeId!!)
        setSavedTheme(themeId!!)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCalculation = findViewById(R.id.tvCalculation)
        tvResult = findViewById(R.id.tvResult)
        buttonPanel = findViewById(R.id.buttonPanel)

        Log.d("config", "savedInstanceState is null: " + (savedInstanceState == null).toString())
        if(savedInstanceState == null) {
            Log.d("config", "before setting height and width")
            // calculate buttonPanel proportions for portrait and land screen orientations
            // and sets buttonPanelHeightPortrait and buttonPanelWidthLand
            Log.d("config", "before calculating proportions")
            // todo these values remain null if configuration changes (since savedInstanceState is not null)
            buttonPanelHeightPortrait = calculateButtonPanelHeightPortrait(resources.configuration)
            Log.d("config", "set buttonPanelHeightPortrait: " + buttonPanelHeightPortrait.toString())
            buttonPanelWidthLand = calculateButtonPanelWidthLand(resources.configuration)
            Log.d("config", "set buttonPanelWidthLand: " + buttonPanelWidthLand.toString())
            Log.d("config", "calling onConfigurationChanged()")
        } else {
            buttonPanelHeightPortrait = savedInstanceState.getInt("buttonPanelHeightPortrait")
            buttonPanelWidthLand = savedInstanceState.getInt("buttonPanelWidthLand")
        }

        // set the UI buttonPanel proportions, but only if onCreate() is not called by recreate to
        // set the theme, because the theme does not change layout proportions
        // Check if the device is in portrait mode
        if(!themeChangedRecreated!!) {
            onConfigurationChanged(resources.configuration)
        }

        // set all onClickListeners for the digit buttons 1 - 9
        arrayOf(
            R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9
        ).forEach {
            setListenerDigitsNonZero(findViewById(it))
        }

        // zero button
        setListenerZero()

        // brackets button: ()
        setListenerBrackets()

        // comma button: ,
        setListenerComma()

        // set the onClickListeners of the buttons mul, div, pow, prc
        arrayOf(
            R.id.buttonMul,
            R.id.buttonDiv,
            R.id.buttonPow,
            R.id.buttonPrc
        ).forEach { setListenersMulDivPowPrc(findViewById(it)) }

        // setting listeners of add and sub
        setListenersAddSub(findViewById(R.id.buttonAdd))
        setListenersAddSub(findViewById(R.id.buttonSub))

        // delete button: DEL
        setListenerDel()

        // clear button: C
        setListenerClear()

        // equals button: =
        setListenerEquals()

        // themes button
        setListenerThemes()
    }


    // set action listeners of the buttons:

    // general function to set onClickListeners for the digit buttons 1 - 9
    private fun setListenerDigitsNonZero(button: Button) {
        button.setOnClickListener { // todo add textSize adjustments for all text altering buttons
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length > maxCharAmount) {
                Toast.makeText(
                    this,
                    "Cannot display more than $maxCharAmount characters.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                var calculationText = when {
                    lastChar == null -> button.text
                    lastChar in setOf(')', '%') -> "${tvCalculation.text}×${button.text}"
                    lastChar == '0' && (tvCalculation.text.length == 1 ||
                            !(tvCalculation.text[tvCalculation.text.lastIndex.minus(1)].isDigit()
                                    || tvCalculation.text.firstNonDigitCharFromEnd(',')))
                    -> tvCalculation.text.dropLast(1).append(button.text)

                    else -> "${tvCalculation.text}${button.text}"
                }
                //Log.d("calculationText button", calculationText.toString())
                //Log.d("refresh", calculationText.toString())
                // only adds separators to the last token because the previous ones have separators (since calculation)
                calculationText = calculationText.refreshThousandSeparatorsLastToken()
                tvCalculation.text  = calculationText

                //Log.d("refresh", calculationText.toString())
                //Log.d("refreshed", "1.111.1111".removeThousandSeparatorsLastToken().toString())

                // if the sequence in the calculation text is a number that does not need to be calculated, do not display a result
                val result =
                    if (calculationText.removeThousandSeparatorsLastToken().isNumeric()) "" else calculationText.calculate()

                // set the content of result to tvResult.text if the result is valid
                result.displayResultIfValid()
            }
        }
    }

    // sets the onClick listener for the zero button
    private fun setListenerZero() {
        findViewById<Button>(R.id.button0).setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length < maxCharAmount) {
                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                val calculationText: CharSequence = when {
                    lastChar == null -> "0"
                    lastChar in setOf(
                        ')',
                        '%'
                    ) -> "${tvCalculation.text}×0" // todo is append() more efficient?
                    // do nothing if a new zero is added with no number directly before it
                    (tvCalculation.text == "0") ||
                            (lastChar == '0' && tvCalculation.text.length > 1
                                    && !((tvCalculation.text[tvCalculation.text.lastIndex - 1].isDigit() ||
                                    tvCalculation.text.firstNonDigitCharFromEnd(',')))) -> tvCalculation.text

                    else -> "${tvCalculation.text}0"
                }
                // only adds separators to the last token because the previous ones have separators (since calculation)
                tvCalculation.text = calculationText.refreshThousandSeparatorsLastToken()
                // if the sequence in the calculation text is a number that does not need to be calculated, do not display a result
                val resultText =
                    if (calculationText.isNumeric()) "" else calculationText.calculate()

                // set the content of result to tvResult.text if the result is valid
                resultText.displayResultIfValid()
            }
        }
    }

    // sets the onClick listener for the brackets button
    private fun setListenerBrackets() {
        findViewById<Button>(R.id.buttonBrackets).setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length < maxCharAmount) {

                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                tvCalculation.text = when {
                    lastChar == null -> "("
                    // if the last value is part of an incomplete exponent in the scientific notation, do not add anything
                    tvCalculation.text.lastNumberHasExponent() && !lastChar.isDigit() -> tvCalculation.text
                    lastChar.isDigit() || lastChar == '%' || lastChar == ')' ->
                        if (tvCalculation.text.bracketPicker() == "(") "${tvCalculation.text}×(" else "${tvCalculation.text})"

                    lastChar == '(' || lastChar.isOperator() -> "${tvCalculation.text}("
                    // comma: remove the comma because it is unused if bracket follows
                    else -> if (tvCalculation.text.bracketPicker() == "(")
                        "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)}×("
                    else "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)})"
                }
                // notify user about invalid expression if he tries to add a bracket to a scientific notation exponent
                // if it is incomplete (lastChar is not a digit)
                if (tvCalculation.text.lastNumberHasExponent() && lastChar != null && !lastChar.isDigit()) {
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // sets onClick listener for the comma button
    private fun setListenerComma(){
        findViewById<Button>(R.id.buttonCom).setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length < maxCharAmount) {
                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                if (!tvCalculation.text.firstNonDigitCharFromEnd(',')) {
                    val (calculationText, resultText) = when {
                        // if tvCalculation!! is empty and user types "," -> shows "0," (incomplete expression)
                        lastChar == null -> "0," to "0"
                        // if the last value is in part of the exponent in the scientific notation
                        // or is a comma, do not add anything
                        tvCalculation.text.lastNumberHasExponent() -> tvCalculation.text to tvResult.text
                        // lastChar is opening bracket '(' or an operator
                        lastChar == '(' || lastChar.isOperator() -> "${tvCalculation.text}0," to "${tvCalculation.text}0".calculate()
                        lastChar == ')' || lastChar == '%' -> "${tvCalculation.text}×0," to "${tvCalculation.text}×0".calculate()
                        // if lastChar is digit, just append the comma
                        else -> "${tvCalculation.text}," to tvResult.text
                    }
                    if (tvCalculation.text.lastNumberHasExponent()) {
                        Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                    }
                    // todo encapsulate in function for the digit buttons, comma
                    // update the TextViews
                    tvCalculation.text = calculationText

                    // set the content of result to tvResult.text if the result is valid
                    resultText.displayResultIfValid()
                } else {
                    // if the last number already contains a comma and it is not the last Char, do not
                    // alter any text and notify the user
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // sets the onClickListeners of the buttons mul, div, pow, prc
    private fun setListenersMulDivPowPrc(button: Button) {
        button.setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length < maxCharAmount) {

                val operator =
                    if (button.text.length > 1) "^(" else button.text  // pow is a special case
                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null

                // becomesInvalid -> keeps track of whether the expression becomes invalid after click action
                val (calculationText, becomesInvalid) = when {
                    // invalid: operator cannot be the first Char of an expression or come after '('
                    lastChar == null || lastChar == '(' -> tvCalculation.text to true

                    tvCalculation.text.lastNumberHasExponent() ->
                        // if the last value is part of a complete exponent in the scientific notation,
                        // append normally but put the current expression in brackets
                        if (lastChar.isDigit()) {
                            "(${tvCalculation.text})$operator" to false
                        } else {
                            // if the last value is part of an incomplete exponent in the scientific notation, do not add anything
                            tvCalculation.text to true
                        }
                    // do nothing if lastChar is already the pressed button, can't have double operators
                    lastChar == operator[0] -> tvCalculation.text to false
                    // invalid: if the second last Char is a '(' with a '-' or '+' following, remove the last Char
                    (lastChar == '+' || lastChar == '-') && tvCalculation.text.length > 1 &&
                            tvCalculation.text[tvCalculation.text.lastIndex - 1] == '('
                    -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex) to true
                    // if the last Char is an operator or a ',', replace it with '[op]' ('[op](' for ^)
                    lastChar.isOperator() || lastChar == ','
                        // if there already is a '%' before the lastChar (which is an operator), do not add another one
                    -> if(operator == "%" && tvCalculation.text.length > 2 && tvCalculation.text[tvCalculation.text.length - 2] == '%') {
                        tvCalculation.text to true
                    } else {
                        // else just replace the old operator with the new one (note that '%' is not classified as an operator here)
                        "${tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)}${operator}" to false
                    }
                    // if the last Char is a digit, ')' or a '%', just append with '[op]('
                    else -> "${tvCalculation.text}${operator}" to false
                }
                // notify the user about invalid expression
                if (becomesInvalid) {
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                }
                tvCalculation.text = calculationText
                // (only "%" changes the result of the expression, and requires the result view to refresh)
                // only performs a calculation if the expression is not a single numerical value
                // (there is something to calculate), this can be the case if '%' has not been appended
                // to the expression (i.e. 5 -> () -> % leads to 5 * ( because '%' cannot be added after
                // a '('

                if (tvCalculation.text.isNotEmpty() && operator == "%" && (tvCalculation.text.last() == '%' || (!tvCalculation.text.isSingleNumericalValue() &&
                            tvCalculation.text.last() != '%'))) {
                    val result = tvCalculation.text.calculate()

                    // if the result is an imaginary number or causes an infinity value, do not display any result
                    result.displayResultIfValid()
                }
            }
        }
    }

    // function to set the listeners for the add and sub buttons
    private fun setListenersAddSub(button: Button) {
        val thisOperator = button.text
        val otherOperator = if (button.text == "+") "-" else "+"
        var invalidExponent = false

        button.setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.length < maxCharAmount) {
                val lastChar =
                    if (tvCalculation.text.isNotEmpty()) tvCalculation.text.last() else null
                tvCalculation.text = when {
                    lastChar == null -> "($thisOperator"

                    tvCalculation.text.lastNumberHasExponent() ->
                        // if the last value is part of a complete exponent in the scientific notation,
                        // append normally but put the current expression in brackets
                        if (lastChar.isDigit()) {
                            "(${tvCalculation.text})$thisOperator"
                        }
                        // only append after 'E' if this operator is '-'
                        else if (lastChar == 'E' && thisOperator == "-") {
                            "${tvCalculation.text}-"
                        } else {
                            // else, do not add anything
                            tvCalculation.text.apply {
                                invalidExponent = true
                            } // todo use this in other code parts
                        }

                    // if [thisOperator] is the first Char or comes after another operator (not [thisOperator] or '%'), add a '(' before it
                    ((thisOperator == "-" && lastChar.isOperator()
                            && lastChar != thisOperator[0])) -> tvCalculation.text.append("($thisOperator")

                    // if the second last Char is a '(' with a [otherOperator] following, replace the last Char
                    (tvCalculation.text.length >= 2) && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == '(') &&
                            (tvCalculation.text.last() == otherOperator[0])
                    -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                        .append(thisOperator)
                    // if the last Char is a digit, a ')', '(' or '%', just append with [thisOperator]
                    lastChar.isDigit() || lastChar == ')' || lastChar == '(' || lastChar == '%'
                    -> tvCalculation.text.append(thisOperator)
                    // if last Char is a comma, remove the comma, or for plus: last operator gets replaced
                    else -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex)
                        .append(thisOperator)
                }
                if (invalidExponent) {
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // sets onClick listener for the DEL button
    private fun setListenerDel(){
        findViewById<Button>(R.id.buttonDel).setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.isNotEmpty()) {
                // if the last Chars are '^' and '(' following, remove both, else remove only one last Char
                tvCalculation.text = when {
                    tvCalculation.text.length > 1 && tvCalculation.text[tvCalculation.text.lastIndex - 1] == '^'
                            && tvCalculation.text.last() == '('
                    -> tvCalculation.text.subSequence(0, tvCalculation.text.lastIndex - 1).refreshThousandSeparatorsLastToken()
                    // else remove only the last Char
                    else -> tvCalculation.text.dropLast(1).refreshThousandSeparatorsLastToken()
                }
                // invalid scientific number in calculation view
                val invalidExpression = if (tvCalculation.text.isNotEmpty()) {
                    tvCalculation.text.last() == 'E' || tvCalculation.text.length > 1
                            && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == 'E')
                            && tvCalculation.text.last() == '-'
                } else {
                    false
                }
                // if the calculation text is empty, invalid or already the result (numeric without a %-operator), set the result as empty
                val result = if (tvCalculation.text.isEmpty() || invalidExpression ||
                    (tvCalculation.text.isNumeric() && !tvCalculation.text.contains('%'))
                ) "" else tvCalculation.text.calculate()

                // set the content of result to tvResult.text if the result is valid
                result.displayResultIfValid()
            }
        }
    }

    // sets onClick listener for the clear button (C)
    private fun setListenerClear(){
        findViewById<Button>(R.id.buttonC).setOnClickListener {
            // set onClick vibration
            vibrate()

            tvCalculation.text = ""
            tvResult.text = ""
        }
    }

    // set onClick listener for the equals button (=)
    private fun setListenerEquals(){
        findViewById<Button>(R.id.buttonEq).setOnClickListener {
            // set onClick vibration
            vibrate()

            if (tvCalculation.text.isNotEmpty()) {
                val result: CharSequence

                // invalid scientific number in calculation view
                val invalidExpression =
                    tvCalculation.text.last() == 'E' || tvCalculation.text.length > 1
                            && (tvCalculation.text[tvCalculation.text.lastIndex - 1] == 'E')
                            && tvCalculation.text.last() == '-'
                if (invalidExpression) {
                    result = tvCalculation.text
                    Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show()
                } else {
                    result = tvCalculation.text.calculate()
                }

                // if the result is out of range, empty everything and notify the user
                when (result) {
                    "outOfRange" -> apply {
                        tvCalculation.text = ""
                        Toast.makeText(
                            this,
                            "Cannot calculate outside of the allowed range.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    "divisionByZero" -> Toast.makeText(
                        this,
                        "Cannot divide by zero.",
                        Toast.LENGTH_SHORT
                    ).show()

                    "imaginaryNumber" -> Toast.makeText(
                        this,
                        "Imaginary numbers are not allowed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> tvCalculation.text = result.refreshThousandSeparatorsLastToken()
                }
                tvResult.text = ""
            }
        }
    }

    private fun setListenerThemes() {
        findViewById<Button>(R.id.buttonThemes).setOnClickListener {
            // todo
            Log.d("themes clicked", "")
            themeId = R.style.Theme_Lavender
            Log.d("theme set to", themeId.toString())
            recreate()
        }
    }

    // sets tvResult.text to the result (this) if the result is valid and refreshes the thousand separators
    private fun CharSequence.displayResultIfValid(){
        if (this.isEmpty() || this == "outOfRange" || this == "divisionByZero" || this == "imaginaryNumber") {
            tvResult.text = ""
        } else {
            tvResult.text = this.refreshThousandSeparatorsLastToken()
        }
    }

    // groups three digits separated by a dot in a CharSequence expression
    private fun CharSequence.addThousandSeparatorsLastToken(): String {
        var separatorsAddedToken = StringBuilder()

        // only the last token is altered here because this function is called each time a digit is added,
        // so the previous tokens already have separators
        val tokenList = this.tokenList(false)
        //Log.d("tokenList() result", tokenList.toString())

        var lastToken: CharSequence
        try {
            // last token to add separators to
            lastToken = tokenList.last()
            tokenList.removeLast()
        }
        // NoSuchElementException thrown if tokenList is empty
        catch(e: NoSuchElementException) {
            return ""
        }

        // if the last token cannot contain thousand separators, return the CharSequence, unchanged
        if(!lastToken.isNumeric()) {
            return this.toString()
        }

        var digitCounter = 0
        var hasComma = false
        var commaWithDecimalPart = "" as CharSequence

        if(lastToken.contains(',')) {
            hasComma = true
            commaWithDecimalPart = lastToken.subSequence(lastToken.indexOf(','))
            lastToken = lastToken.subSequence(0, lastToken.indexOf(','))
        }

        for(index in lastToken.lastIndex downTo 0) {
            separatorsAddedToken = separatorsAddedToken.insert(0, lastToken[index])
            if(lastToken[index].isDigit()) {
                digitCounter++
                if(digitCounter == 3 && index != 0 && lastToken[index - 1].isDigit()) {
                    separatorsAddedToken = separatorsAddedToken.insert(0, '.')
                    digitCounter = 0
                }
            } else {
                digitCounter = 0
            }
        }
        if(hasComma) {
            separatorsAddedToken.append(commaWithDecimalPart)
        }
        tokenList.add(separatorsAddedToken)

        //Log.d("tokenList added", tokenList.joinToString())
        return tokenList.joinToString("")
    }

    // remove the added thousand separators to process the result in the calculation view (with equals)
    private fun CharSequence.removeThousandSeparatorsLastToken(): String {
        //val removedSeparatorsString = StringBuilder()

        // only the last token is altered here because this function is called each time a digit is added,
        // so the previous tokens already have separators
        val tokenList = this.tokenList(false)
        //Log.d("tokenList() result", tokenList.toString())

        var lastToken: CharSequence
        try {
            // last token to add separators to
            lastToken = tokenList.last()
            tokenList.removeLast()
        }
        // NoSuchElementException thrown if tokenList is empty
        catch(e: NoSuchElementException) {
            return ""
        }

        lastToken = lastToken.filter { it != '.' }
        tokenList.add(lastToken)

        //Log.d("tokenList removed", tokenList.joinToString())
        return tokenList.joinToString("")
    }

    // removes and adds the thousand separators correctly
    private fun CharSequence.refreshThousandSeparatorsLastToken(): String {
        //Log.d("BEFORE", this.toString())
        //Log.d("AFTER", removeThousandSeparatorsLastToken().addThousandSeparatorsLastToken())
        return removeThousandSeparatorsLastToken().addThousandSeparatorsLastToken()
    }

    // removes all thousand separators for calculation
    private fun ArrayList<CharSequence>.removeThousandSeparatorsAll(): ArrayList<CharSequence> {
        val dotsRemovedList = ArrayList<CharSequence>()
        //Log.d("BEFORE", this.joinToString())
        this.forEach { seq -> dotsRemovedList.add(seq.filter{ it != '.'}.toString()) }
        //Log.d("AFTER", dotsRemovedList.joinToString())
        return dotsRemovedList
    }

    // calculates the result of the expression by formatting, tokenizing it and calling
    // the recursive ArrayList<CharSequence>.calculate() function

    // CAUTION: Can return a CharSequence representing a numerical result
    // OR the label "outOfRange", which indicates that the result of the calculation
    // (or a part of it) is too large or too small
    // -> "outOfRange" results from a NumberFormatException thrown in toNumber() or any of the
    // operator functions
    private fun CharSequence.calculate(): CharSequence {
        //Log.d("before", tokenList().joinToString())
        // remove the thousand separators for the calculation
        // todo remove all dots before calling toNumber()
        val tokenList = tokenList(true).removeThousandSeparatorsAll()

        Log.d("after", tokenList.joinToString())

        val resultList: ArrayList<CharSequence>
        try {
            resultList = tokenList.calculate(true)
        } catch (e: ArithmeticException) {
            // returns a label so caller function can decide whether to notify the user or not
            return "divisionByZero"
        } catch (e: ClassNotFoundException) {
            // returns a label so caller function can decide whether to notify the user or not
            return "imaginaryNumber"
        } catch (e: NumberFormatException) {
            // returns a label so caller function can decide whether to notify the user or not
            return "outOfRange"
        }

        return if (resultList.isEmpty()) {
            ""
        } else {
            // replace the dot with a comma if available
            var result = resultList[0]
            resultList.clear()
            // if last expression is percentage resolve percentages via toNumber
            if (result[result.lastIndex] == '%') {
                result = result.toDouble().toString()
            }
            // format the result value (removes unnecessary decimal places, also rounding)
            result = result.formatNumber()
            // remove unary operators from 0 caused by prior calculations
            if(result == "-0" || result == "+0") {
                result = "0" // remove first Char
            }
            return result.replace(Regex("\\."), ",")
        }
    }

    // calculates result of formatted and tokenized expression  recursively
    @Throws(NumberFormatException::class, ArithmeticException::class)
    private fun ArrayList<CharSequence>.calculate(firstCall: Boolean): ArrayList<CharSequence> {
        var tokenList = this
        //Log.d("tokenListCalc", tokenList.joinToString())

        if (tokenList.isEmpty()) {
            return ArrayList()
        }
        // recursion end -> last element as result
        else if (tokenList.size == 1) {
            return tokenList
        }
        // merge tokens except on the initial recursive call, because expression has not been altered since tokenization
        if (!firstCall) {
            tokenList = tokenList.mergePercentages().mergePlusMinus()
        }

        // test for StringBuilder
       /*tokenList.forEach { element ->
            Log.d("elementType", element::class.java.name)
            Log.d("elementValue", element.toString())
        }

        Log.d("tokenList", tokenList.joinToString())
        Log.d("containsBr", tokenList.any { it.toString() == "(" }.toString())*/

        // resolve the brackets
        if (tokenList.contains("(")) {

            val startIndex = tokenList.indexOf("(")

            // calculates index of the closing bracket that corresponds to the opening bracket at startIndex
            var endIndex = tokenList.lastIndex
            var bracketCounter = -1

            for (index in (startIndex + 1)..tokenList.lastIndex) {
                if (tokenList[index] == "(") {
                    bracketCounter--
                } else if (tokenList[index] == ")") {
                    bracketCounter++
                }
                if (bracketCounter == 0) {
                    endIndex = index
                    break
                }
            }

            val leftExpression: ArrayList<CharSequence> = tokenList.subListExtension(0, startIndex)

            val rightExpression =
                if (endIndex < tokenList.lastIndex) {
                    tokenList.subList(endIndex + 1)
                } else {
                    ArrayList()
                }

            val innerExpression = tokenList.subListExtension(startIndex + 1, endIndex)

            val resultList = ArrayList<CharSequence>().apply {
                // no brackets here, but expression ends with an operator likely
                if (leftExpression.isNotEmpty()) {
                    addAll(leftExpression)
                }
                addAll(innerExpression.calculate(false)) // more brackets may be there
                // no brackets here, but expression ends with an operator likely
                if (rightExpression.isNotEmpty()) {
                    addAll(rightExpression)
                }
            }
            rightExpression.clear()
            innerExpression.clear()
            leftExpression.clear()

            // if resultList still contains brackets (that were not all nested), keep resolving the brackets
            if (resultList.contains("(")) {
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

        val calculatedTokenList: ArrayList<CharSequence> = tokenList
        while (tokenList.contains("×") || tokenList.contains("÷") || tokenList.contains("^")
            || tokenList.contains("+") || tokenList.contains("-")) {

            tokenList = calculatedTokenList
            val operatorIndex: Int =
                // multiplication, division exponentiation: same precedence (from left to right)
                if (tokenList.contains("^")) { //"^" has highest precedence
                    tokenList.indexOfFirst { it == "^" }
                } else if (tokenList.contains("×") || tokenList.contains("÷")) {
                    tokenList.indexOfFirst {
                        it == "×" || it == "÷"
                    }
                }

                // add and sub (binary) have lower precedence
                else {
                    tokenList.indexOfFirst { it == "+" || it == "-" }
                }
            // assumes that each operator is surrounded by numbers left and right
            val operand1 = tokenList.elementAt(operatorIndex - 1).toDouble()
            val operand2 = tokenList.elementAt(operatorIndex + 1).toDouble()

            val result = when (tokenList.elementAt(operatorIndex)) {
                "^" -> (operand1).powerThrowsException(operand2)
                "×" -> (operand1).mulThrowsException(operand2)
                "÷" -> // may throw exception, which is handled by callee
                    (operand1).divThrowsException(operand2)

                "+" -> (operand1).addThrowsException(operand2)
                else -> (operand1).subThrowsException(operand2)
            }
            // remove both operands and operator and insert result
            repeat(3) { calculatedTokenList.removeAt(operatorIndex - 1) }
            calculatedTokenList.add(operatorIndex - 1, "$result")
        }

        //Log.d("result", newTokenList[0].toString())
        return calculatedTokenList
    }

    // returns true if CharSequence is a negative/positive integer/decimal, else false
    // caution: does not specify how 'E' and ',' or '.' can be positioned in the CharSequence
    // -> e.g. "4,E4" would return true todo
    private fun CharSequence.isNumeric(): Boolean {
        var commas = 0
        for (i in this.indices) {
            // first Char has to be a digit, '+' or '-'
            if (i == 0 && (this[i] == '-' || this[i] == '+' || this[i].isDigit())) {
                continue
            }
            // count comma
            else if ((this[i] == ',' || this[i] == '.') && commas == 0 && i != 0 && i != this.lastIndex
                && this[i - 1].isDigit() && this[i + 1].isDigit()
            ) {
                commas++
                continue
            }
            // '%', 'E', and '-' following 'E' are allowed
            else if ((i == lastIndex && this[i] == '%') ||
                (i != lastIndex && i != 0 && this[i] == 'E') ||
                (i != lastIndex && i != 0 && this[i] == '-' && this[i - 1] == 'E')
            ) {
                continue
            }
            // Char is not a digit (or 'E' or 'E' + '-'), or it is a second comma -> false
            else if (!this[i].isDigit()) {
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
        forEach { char -> if (char == '(') count-- }
        forEach { char -> if (char == ')') count++ }
        if (count == 0 || count < 0 && this[lastIndex] == '(') {
            return "("
        }
        return ")"
    }

    // helper function for ',' or '.': ensures a number has a maximum of one ',' or '.'
    private fun CharSequence.firstNonDigitCharFromEnd(commaOrDot: Char): Boolean {
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
        return char == commaOrDot
    }

    // if needed, removes incomplete expression Chars at the end and adds missing closing brackets
    // to prepare for calculate() function
    private fun CharSequence.formatExpression(): CharSequence {
        if (this.isEmpty()) {
            return this
        }
        var exprToFormat = this
        // remove last Char if it doesn't complete the expression
        while (exprToFormat.isNotEmpty() && !(exprToFormat.last() == ')' || exprToFormat.last()
                .isDigit() ||
                    exprToFormat.last() == '%')
        ) {
            exprToFormat = exprToFormat.subSequence(0, exprToFormat.lastIndex)
        }
        // add the missing closing brackets if needed
        while (exprToFormat.contains('(') && exprToFormat.bracketPicker() == ")") {
            exprToFormat = exprToFormat.append(")")
        }
        return exprToFormat.toString()
    }

    // helper function to convert CharSequence into Double or Int values
    // CAUTION: throws IllegalArgumentException if the CharSequence is not numeric
    @Throws(NumberFormatException::class)
    private fun CharSequence.toDouble(): Double {
        var seq = this
        var number: Double
        var isPercentage = false
        if (!seq.isNumeric()) {
            throw IllegalArgumentException("CharSequence.toNumber() cannot be called on a non-numeric CharSequence")
        }
        // replace ',' with '.'
        if (seq.contains(',')) {
            seq = seq.replace(Regex(","), ".")
        }
        // if number is a percentage, remember it for later and remove the percentage
        if (seq.last() == '%') {
            isPercentage = true
            seq = seq.subSequence(0, seq.lastIndex)
        }
        // returns Double
        if (seq.toString().toDoubleOrNull() != null) {
            number = seq.toString().toDoubleOrNull() ?: Double.NaN
        } else {
            // will never be reached because seq.isNumeric() must be true (see above)
            return Double.NaN
        }
        if (isPercentage) {
            val result = number / 100
            // throws exception if result is infinity (distinction between pos./neg. infinity)
            throwExceptionIfInfinity(result)
            number = result
        }
        return number
    }

    // converts a CharSequence expression into a list of CharSequence tokens
    // CAUTION: contains thousand separators '.', which need to be removed before calculation
    @Throws(IllegalArgumentException::class)
    private fun CharSequence.tokenList(formatExpression: Boolean): ArrayList<CharSequence> {
        // Only call on formatted expression and without thousand separators
        val expression = if (formatExpression) this.formatExpression() else this
        Log.d("expression", expression.toString())

        val newList = ArrayList<CharSequence>()
        var number = ""

        for (index in expression.indices) {
            // append current number, '+','-', 'E', ',' or '%' sign or the thousand separator '.'
            if (expression[index].isDigit() || expression[index] == ',' ||
                (index == 0 && (expression[0] == '-' || expression[0] == '+') && expression[1].isDigit()) ||
                index > 0 && ((index + 1 in indices && (expression[index - 1] == '(' || expression[index - 1] == 'E')
                        && (expression[index] == '-' || expression[index] == '+') && expression[index + 1].isDigit()) ||
                        (expression[index - 1].isDigit() && (expression[index] == '%' || expression[index] == 'E')))
                || expression[index] == '.'
            ) {
                number += expression[index]
            }
            // add number to tokenList
            else {
                if (number.isNotEmpty()) {
                    if (number.last() == 'E') {
                        throw IllegalArgumentException("Invalid expression.")
                    }
                    newList.add(number)
                    number = ""
                }
                // add operator or bracket to tokenList
                newList.add(expression[index].toString())
            }
        }
        if (number.isNotEmpty()) {
            newList.add(number)
        }
        Log.d("newList", newList.joinToString())
        return newList
    }

    // custom function for ArrayList<CharSequence> class
    // -> returns a subList that contains all elements starting from the specified index
    private fun ArrayList<CharSequence>.subList(fromIndex: Int): ArrayList<CharSequence> {
        // if index is out of range, throws IllegalArgumentException
        if (fromIndex !in this.indices) {
            throw IllegalArgumentException(
                "fromIndex in ArrayList<CharSequence>.subList(fromIndex: Int) is out of bounds"
            )
        }
        val subList = ArrayList<CharSequence>()
        for (i in fromIndex..this.lastIndex) {
            subList.add(this[i])
        }
        return subList
    }

    // custom extension function for ArrayList<CharSequence> class
    // -> returns a subList that contains all elements starting from the startIndex until the endIndex (exclusive)
    private fun ArrayList<CharSequence>.subListExtension(
        startIndex: Int,
        endIndex: Int
    ): ArrayList<CharSequence> {
        // if index is out of range, throws IllegalArgumentException
        if (startIndex !in this.indices || endIndex !in this.indices) {
            Log.d("startIndex", startIndex.toString())
            Log.d("endIndex", endIndex.toString())
            Log.d("this.indices", indices.joinToString())

            throw IllegalArgumentException(
                "indices in ArrayList<CharSequence>.subList(startIndex: Int, endIndex: Int) out of bounds"
            )
        }
        val subList = ArrayList<CharSequence>()
        for (i in startIndex..<endIndex) {
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
                "fromIndex in ArrayList<CharSequence>.subList(fromIndex: Int) is out of bounds"
            )
        }
        val subSequence = StringBuilder()
        for (i in fromIndex..this.lastIndex) {
            subSequence.append(this[i])
        }
        return subSequence
    }

    // multiplication
    @Throws(NumberFormatException::class)
    private fun Double.mulThrowsException(other: Double): Double {
        val result = this * other
        // throws exception if result is infinity
        throwExceptionIfInfinity(result)
        return result
    }

    // division
    @Throws(NumberFormatException::class, ArithmeticException::class)
    private fun Double.divThrowsException(other: Double): Double {
        if (other == 0.0) {
            throw ArithmeticException("Cannot divide by zero in Number.div(other: Number)")
        }
        val result = this / other
        // throws exception if result is infinity
        throwExceptionIfInfinity(result)
        return result
    }

    // exponentiation
    @Throws(NumberFormatException::class)
    private fun Double.powerThrowsException(other: Double): Double {
        // if the base is negative and the exponent is not an integer, the result would be a complex
        // number -> notify the user
        if (this < 0 && other - other.toInt() != 0.0) {
            // special case of the pow() function: if base is < 0 and exp is not an Int -> returns NaN
            throw ClassNotFoundException("No support for complex numbers")
        }
        val result = this.pow(other)
        // throws exception if result is Infinity
        throwExceptionIfInfinity(result)
        return result
    }

    // addition
    @Throws(NumberFormatException::class)
    private fun Double.addThrowsException(other: Double): Double {
        val result = this + other
        // throws exception if result is infinity (distinction between pos./neg. infinity)
        throwExceptionIfInfinity(result)
        return result
    }

    // subtraction
    @Throws(NumberFormatException::class)
    private fun Double.subThrowsException(other: Double): Double {
        val result = this - other
        // throws exception if result is infinity (distinction toNumber between pos./neg. infinity)
        throwExceptionIfInfinity(result)
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
        val unaryMinusPlusFirst = (this[0] == "-" || this[0] == "+") && this[1].isNumeric()
        for (index in this.indices) {
            if ((unaryMinusPlusFirst && index == 1) || index >= 2 && this[index - 2] == "(" && (this[index - 1] == "-"
                        || this[index - 1] == "+") && this[index].isNumeric()
            ) {
                // mul() can throw a NumberFormatException theoretically,
                // but practically never will because we only multiply by +1 or -1
                mergedValue =
                    ("${this[index - 1]}1".toDouble() * this[index].toDouble()).toString()
                tokenList.removeAt(index - 1)
                tokenList.add(mergedValue.toString())
            } else {
                tokenList.add(this[index].toString())
            }
        }
        return tokenList
    } // todo combine both merge functions

    // merges percentage Chars with a number after bracket resolution in calculate()
    private fun ArrayList<CharSequence>.mergePercentages(): ArrayList<CharSequence> {
        val tokenList: ArrayList<CharSequence> = ArrayList()
        for (index in this.indices) {
            // merge numbers with '%'
            if (index > 0 && this[index] == "%" && this[index - 1].isNumeric()) {
                tokenList.removeAt(index - 1)
                tokenList.add("${this[index - 1]}${this[index]}")
            } else {
                tokenList.add(this[index].toString())
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

        if (!this.isNumeric()) {
            throw IllegalArgumentException("CharSequence.formatNumber(): this.isNumeric() has to return true")
        }
        var number = this
        var exponent: CharSequence? = null // remains null if the number has no comma

        if (number.firstNonDigitCharFromEnd(',') || number.firstNonDigitCharFromEnd('.') || number.lastNumberHasExponent()) {

            // replace ',' with  '.' for uniformity of calculation
            number = number.replace(Regex(","), "\\.")

            // if the number is in scientific notation, remove the exponent for rounding the base later
            // and remember the exponent to add it at the end, if there is no scientific notation, save exponent as null
            exponent = if (number.contains('E')) number.subSequence(number.indexOf('E')) else null
            if (exponent != null) {
                // remove exponent
                number = number.subSequence(0, number.indexOf('E'))
            }

            val decimalPoint = number.indexOf(".")
            val postDecimalPoint = number.subSequence(decimalPoint + 1)

            // return as integer if all digits after the decimal point are '0'
            if (exponent == null && postDecimalPoint.all { char -> char == '0' }) {
                return number.subSequence(0, decimalPoint).toString()
            }

            // is double: round to a maximum of 10 decimal places
            if (postDecimalPoint.length > 10) {
                // Locale.US ensures there is a dot in the number and no commas
                number = String.format(Locale.US, "%.10f", number.toDouble())
            }

            // if there are unnecessary '0' at the end after the decimal point or
            // there are only zeros after the comma so the comma is redundant, remove them
            while (number[number.lastIndex] == '0' || number[number.lastIndex] == '.') {
                number = number.subSequence(0, number.lastIndex)
            }

        }
        // if the number had an exponent, add it back
        if (exponent != null) {
            // add a ".0" if the new result is an Int
            if (!number.contains('.')) {
                number = number.append(".0")
            }
            number = number.append(exponent)
        }
        // number is in Int format or in Double format with a non-zero decimal-value
        return number.toString()
    }

    // helper function to append a CharSequence to another CharSequence
    private fun CharSequence.append(other: CharSequence): CharSequence {
        return "${this}${other}"
    }

    // helper function throw NumberFormatException in case of infinity results
    @Throws(NumberFormatException::class)
    private fun throwExceptionIfInfinity(result: Double) {
        if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
            throw NumberFormatException()
        }
    }

    // returns true if the last value/operand in the expression has the scientific notation form
    // false if the last Char is a bracket, as we only consider the raw sequence (not the formatted expression)
    private fun CharSequence.lastNumberHasExponent(): Boolean {
        if (this.isEmpty() || !this.contains('E')) {
            return false
        }
        var result = true
        var hasMinus = false
        for (char in this.last() downTo this.first()) {
            if (char == 'E') {
                break
            }
            if ((char != '-' && !char.isDigit()) || (char == '-' && hasMinus)) {
                result = false
                break
            }
            if (char == '-') {
                hasMinus = true
            }
        }
        return result
    }

    // returns true if the CharSequence (the expression) is already a single numerical value
    // and does not require calculation
    private fun CharSequence.isSingleNumericalValue(): Boolean {
        var firstNumberFinished = false
        val currentNumber: StringBuilder = StringBuilder()
        for(index in indices) {
            if(this[index].isDigit()) {
                if(firstNumberFinished) {
                    return false
                }
                currentNumber.append(this[index])
            } else {
                if(currentNumber.isNotEmpty()) {
                    firstNumberFinished = true
                }
            }
        }
        return true
    }

    // Called when the activity is about to get destroyed (which happens during configuration changes
    // like screen rotations) and saves the values of the TextViews.
    // The Bundle object is a collection of key-value pairs and is later passed to onRestoreInstanceState()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Saves the state of the calculation and result texts
        outState.putString("calculationText", tvCalculation.text.toString())
        outState.putString("resultText", tvResult.text.toString())
        outState.putInt("themeId", themeId!!)
        outState.putBoolean("themeChangedRestored", themeChangedRecreated!!)
        outState.putInt("buttonPanelHeightPortrait", buttonPanelHeightPortrait!!)
        outState.putInt("buttonPanelWidthLand", buttonPanelWidthLand!!)
    }

    // Called after onStart() when the activity is restored,
    // restores the values of the TextViews after configuration changes and
    // receives a Bundle object from onSaveInstanceState()
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restores the state of the calculation and result texts
        tvCalculation.text = savedInstanceState.getString("calculationText")
        tvResult.text = savedInstanceState.getString("resultText")
        themeId = savedInstanceState.getInt("themeId")
        // todo next line figure out if needed
        themeChangedRecreated = savedInstanceState.getBoolean("themeChangedRecreated")
        buttonPanelHeightPortrait = savedInstanceState.getInt("buttonPanelHeightPortrait")
        buttonPanelWidthLand = savedInstanceState.getInt("buttonPanelWidthLand")
    }

    // adjust width or height of the buttonPanel depending on the screen orientation
    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("config",
            "onConfigurationChanged(): " + if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) "portrait" else "land")
        super.onConfigurationChanged(newConfig)

        // Check if the orientation is portrait
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Define the height of the buttonPanel
            defineButtonPanelHeightPortrait()
        } else {
            // land mode: define the width of the buttonPanel
            defineButtonPanelWidthLand()
            // removes the action bar from the top in land mode
            // todo remove comments from there statements
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
        }
    }

    // gets the amount of button rows in the current layout
    // used for calculating the ratio of the buttonPanel
    private fun getButtonRowsAmount(): Int {
        return buttonPanel.childCount
    }

    // gets the amount of button rows in the current layout
    // used for calculating the ratio of the buttonPanel
    private fun getButtonColumnsAmount(): Int {
        val row = buttonPanel.children.first() as ViewGroup?
        return row?.childCount ?: -1
    }

    // calculates the button panel width in land mode (only once in the app lifecycle)
    private fun calculateButtonPanelHeightPortrait(configuration: Configuration): Int {
        Log.d("config", "calculateButtonPanelHeightPortrait()")

        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)

        // aspect ratio of 1.2 makes buttons in 4x5 grid circular
        return (if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            displayMetrics.widthPixels * getButtonRowsAmount() / getButtonColumnsAmount()
        else displayMetrics.heightPixels * getButtonRowsAmount() / getButtonColumnsAmount())
    }

    private fun defineButtonPanelHeightPortrait() {
        //val buttonPanel = buttonPanel
        Log.d("config", "defineButtonPanelHeightPortrait()")

        // set the UI buttonPanel proportions
        val layoutParams = buttonPanel.layoutParams as ConstraintLayout.LayoutParams
        Log.d("config", "layoutParams defined")
        layoutParams.height = buttonPanelHeightPortrait!!
        Log.d("config", "height set")
        buttonPanel.layoutParams = layoutParams
        Log.d("config", "layoutParams set")
    }

    // calculates the button panel width in land mode (only once in the app lifecycle)
    private fun calculateButtonPanelWidthLand(configuration: Configuration): Int {
        Log.d("config", "calculateButtonPanelWithLand()")
        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)

        // aspect ratio of 1.2 makes buttons in 4x5 grid circular
        return (if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            displayMetrics.heightPixels * getButtonColumnsAmount() / getButtonRowsAmount()
        else displayMetrics.widthPixels * getButtonColumnsAmount() / getButtonRowsAmount())
    }

    // sets the button panel width in land mode for each configuration change
    private fun defineButtonPanelWidthLand() {
        Log.d("config", "defineButtonPanelWidthLand()")

        Log.d("config", "buttonPanelWidthLand: " + buttonPanelWidthLand.toString())

        // set the UI buttonPanel proportions
        val layoutParams = buttonPanel.layoutParams as ConstraintLayout.LayoutParams
        Log.d("config", "layoutParams defined")
        layoutParams.width = buttonPanelWidthLand!!
        Log.d("config", "width set")
        buttonPanel.layoutParams = layoutParams
        Log.d("config", "layoutParams set")
    }

    private fun vibrate() {
        // For API level 26 and above
        vibrator?.vibrate(VibrationEffect.createOneShot(vibrationDurationMilliSec, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    // saves themes
    private fun setSavedTheme(themeId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("current_theme", themeId).apply()
        editor.commit()
    }

}