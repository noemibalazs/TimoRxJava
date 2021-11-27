package com.noemi.android.timorxjava.card

import com.noemi.android.timorxjava.card.Utils.express
import com.noemi.android.timorxjava.card.Utils.mastercard
import com.noemi.android.timorxjava.card.Utils.visa

enum class CardType(val cvc: Int) {
    VISA(3),
    MASTERCARD(3),
    EXPRESS(4),
    UNKNOWN(-1);

    companion object {

        fun getCardType(number: String): CardType {
            return when {
                visa.matcher(number).matches() -> VISA
                mastercard.matcher(number).matches() -> MASTERCARD
                express.matcher(number).matches() -> EXPRESS
                else -> UNKNOWN
            }
        }
    }
}