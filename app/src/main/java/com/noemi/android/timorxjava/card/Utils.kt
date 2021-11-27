package com.noemi.android.timorxjava.card


import io.reactivex.rxjava3.core.Observable
import java.lang.NumberFormatException
import java.util.regex.Pattern

object Utils {

    fun andBoth(first: Observable<Boolean>, last: Observable<Boolean>): Observable<Boolean> {
        return Observable.combineLatest(first, last, { a, b -> a && b })
    }

    fun all(
        first: Observable<Boolean>,
        second: Observable<Boolean>,
        last: Observable<Boolean>
    ): Observable<Boolean> {
        return Observable.combineLatest(first, second, last, { a, b, c -> a && b && c })
    }

    fun equals(first: Observable<Any>, last: Observable<Any>): Observable<Boolean> {
        return Observable.combineLatest(first, last, { a, b -> a == b })
    }

    val visa = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$")
    val mastercard = Pattern.compile("^5[1-5][0-9]{14}$")
    val express = Pattern.compile("^3[47][0-9]{13}$")

    fun getCardSum(list: MutableList<Int>): Boolean {
        var sum = 0
        val length = list.size
        list.forEachIndexed { index, _ ->
            var digit = list[length - index - 1]
            if (index % 2 == 1)
                digit *= 2

            sum += if (digit > 9) digit - 9 else digit
        }
        return sum % 10 == 0
    }

    fun cardNumbersToIntegers(number: String): MutableList<Int> {
        val list = mutableListOf<Int>()
        try {
            number.toCharArray().forEach { char -> list.add(char.toInt()) }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return list
    }
}