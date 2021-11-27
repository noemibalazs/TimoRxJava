package com.noemi.android.timorxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.noemi.android.timorxjava.card.CardValidationActivity
import com.noemi.android.timorxjava.flickr.ui.FlickrActivity
import com.noemi.android.timorxjava.subscription.SubscriptionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLaunchCardActivity.setOnClickListener {
            launchActivity(CardValidationActivity::class.java)
        }

        tvLaunchSubscriptionActivity.setOnClickListener {
            launchActivity(SubscriptionActivity::class.java)
        }

        tvLaunchFlickrActivity.setOnClickListener {
            launchActivity(FlickrActivity::class.java)
        }
    }
}