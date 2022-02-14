package com.noemi.android.timorxjava

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding4.view.clicks
import com.noemi.android.timorxjava.card.CardValidationActivity
import com.noemi.android.timorxjava.coffee_break.CoffeeBreaksActivity
import com.noemi.android.timorxjava.file.FileActivity
import com.noemi.android.timorxjava.flickr.ui.FlickrActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checksPermission()

        tvLaunchCardActivity.clicks().subscribe {
            launchActivity(CardValidationActivity::class.java)
        }

        tvLaunchFlickrActivity.clicks().subscribe {
            launchActivity(FlickrActivity::class.java)
        }

        tvLaunchFileActivity.clicks().subscribe {
            launchActivity(FileActivity::class.java)
        }

        tvLaunchCoffeeActivity.clicks().subscribe {
            launchActivity(CoffeeBreaksActivity::class.java)
        }
    }

    private fun checksPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 12)
        } else {
            tvLaunchFileActivity.isEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 12 && permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tvLaunchFileActivity.isEnabled = true
        }
    }
}