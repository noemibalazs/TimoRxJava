package com.noemi.android.timorxjava.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardTypeViewModel : ViewModel() {

    val cardNumberIsValid = MutableLiveData<Boolean>()
    val expirationYear = MutableLiveData<Int>()
    val cardExpirationIsValid = MutableLiveData<Boolean>()
    val cardType = MutableLiveData<CardType>()

    init {
        cardExpirationIsValid.value = true
        cardType.value = CardType.UNKNOWN
    }
}