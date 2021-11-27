package com.noemi.android.timorxjava.flickr.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.jakewharton.rxbinding4.widget.textChanges
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.flickr.adapter.FlickrAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flickr.*

@AndroidEntryPoint
class FlickrActivity : AppCompatActivity() {

    private val viewModel: FlickrViewModel by viewModels()

    private val flickrAdapter by lazy { FlickrAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr)

        ivDone.isEnabled = false
        etSearch.textChanges()
            .map { search -> search.toString() }
            .map { search -> search.length > 3 }
            .subscribe {
                ivDone.isEnabled = it
                Log.d(TAG, "Button is enabled = $it")
            }

        setUpRV()
        initObservables()
        ivDone.setOnClickListener {
            viewModel.getFlickrImages(etSearch.text.toString())
            etSearch.setText("")
        }
    }

    private fun setUpRV() {
        rvFlickr.adapter = flickrAdapter
    }

    private fun initObservables() {
        with(viewModel) {
            progressObserver.observe(this@FlickrActivity, {
                progressBar.isVisible = it
            })

            errorObserver.observe(this@FlickrActivity, {
                Toast.makeText(this@FlickrActivity, it, Toast.LENGTH_LONG).show()
            })

            flickrImages.observe(this@FlickrActivity, {
                flickrAdapter.submitList(it)
            })
        }
    }

    companion object {
        private val TAG = FlickrActivity::class.simpleName
    }
}