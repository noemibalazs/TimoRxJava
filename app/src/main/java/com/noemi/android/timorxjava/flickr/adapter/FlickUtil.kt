package com.noemi.android.timorxjava.flickr.adapter

import androidx.recyclerview.widget.DiffUtil
import com.noemi.android.timorxjava.flickr.model.FlickrImage

class FlickUtil : DiffUtil.ItemCallback<FlickrImage>() {

    override fun areContentsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: FlickrImage, newItem: FlickrImage): Boolean {
        return oldItem == newItem
    }
}