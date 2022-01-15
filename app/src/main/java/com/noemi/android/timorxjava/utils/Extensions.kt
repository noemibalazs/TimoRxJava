package com.noemi.android.timorxjava

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

fun Context.launchActivity(dest: Class<*>) {
    this.startActivity(Intent(this, dest))
}

fun AppCompatActivity.showDialogFragment(dialogFragment: DialogFragment, tag: String) {
    val transaction = this.supportFragmentManager.beginTransaction()
    val fragment = this.supportFragmentManager.findFragmentByTag(tag)
    fragment?.let {
        transaction.remove(it)
    }
    dialogFragment.show(transaction, tag)
}

