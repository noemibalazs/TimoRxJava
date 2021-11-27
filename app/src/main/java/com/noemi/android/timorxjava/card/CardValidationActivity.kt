package com.noemi.android.timorxjava.card

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.widget.textChanges
import com.noemi.android.timorxjava.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_card_validation.*
import java.util.*
import java.util.concurrent.TimeUnit

class CardValidationActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()

    private val viewModel by lazy { ViewModelProvider(this).get(CardTypeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_validation)

        initSpinners()
        spinnersListeners()
        initObservers()

        cardIsValid()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.cardNumberIsValid.value = it
            }

        tvSubmit.setOnClickListener {
            when {
                viewModel.cardExpirationIsValid.value == true && viewModel.cardNumberIsValid.value == true -> {
                    Toast.makeText(this, getString(R.string.txt_card_is_valid_toast), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.cardExpirationIsValid.value == false && viewModel.cardNumberIsValid.value == true -> {
                    Toast.makeText(this, getString(R.string.txt_check_card_validity_toast), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.cardExpirationIsValid.value == true && viewModel.cardNumberIsValid.value == false -> {
                    Toast.makeText(
                        this,
                        getString(R.string.txt_card_number_check_toast),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.txt_card_not_valid_toast),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    private fun initSpinners() {
        val months = resources.getStringArray(R.array.card_months)
        val years = getYears()
        spMonths.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)
        spYears.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
    }

    private fun spinnersListeners() {
        val months = resources.getStringArray(R.array.card_months)

        val month = calendar.get(Calendar.MONTH) + 1
        Log.d(TAG, "Current month: $month")

        val updatedMonth = if (month < 10) "0$month" else "$month"
        val value = months.asList().first { it == updatedMonth }

        val initPosition = months.toList().indexOf(value)
        spMonths.setSelection(initPosition)
        spYears.setSelection(0)

        spMonths.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                viewModel.cardExpirationIsValid.value =
                    !(position < initPosition && viewModel.expirationYear.value == 0)
                Log.d(TAG, "Selected month index: $position - init position: $initPosition")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                viewModel.expirationYear.value = position
                Log.d(TAG, "Selected year index: $position")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun getYears(): MutableList<Int> {
        val years = mutableListOf<Int>()
        var year = calendar.get(Calendar.YEAR) % 100
        for (i in 0 until 10) {
            years.add(year++)
        }
        return years
    }

    private fun cardIsValid(): Observable<Boolean> {
        etCardNumber.isFocusableInTouchMode = true
        etCardNumber.requestFocus()

        val isValidCardType: Observable<Boolean> =
            etCardNumber.textChanges().debounce(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).map {
                    viewModel.cardType.value = CardType.getCardType(it.toString())
                }.map {
                    viewModel.cardType.value != CardType.UNKNOWN
                }

        // for some reason checked on my own card the algorithm not working
        // so I will ignore it, otherwise use the all() method instead of the andBoth()
        val isCheckSumValid: Observable<Boolean> = etCardNumber.textChanges().map {
            Utils.cardNumbersToIntegers(it.toString())
        }.map {
            Utils.getCardSum(it)
        }

        etCardCVC.isFocusableInTouchMode = true
        etCardCVC.requestFocus()
        val cvcIsValid: Observable<Boolean> = etCardCVC.textChanges().map {
            it.toString().length == viewModel.cardType.value?.cvc
        }

        return Utils.andBoth(isValidCardType, cvcIsValid)
    }

    private fun initObservers() {
        with(viewModel) {
            cardNumberIsValid.observe(this@CardValidationActivity, {
                Log.d(TAG, "Card number is valid: $it")
            })

            cardExpirationIsValid.observe(this@CardValidationActivity, {
                Log.d(TAG, "Card expire date is valid: $it")
            })

            cardType.observe(this@CardValidationActivity, {
                Log.d(TAG, "Card type: ${it.name}")
            })
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
    }
}