package com.noemi.android.timorxjava

import android.content.Context
import android.content.Intent

fun Context.launchActivity(dest: Class<*>) {
    this.startActivity(Intent(this, dest))
}

