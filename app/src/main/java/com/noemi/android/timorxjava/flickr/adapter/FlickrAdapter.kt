package com.noemi.android.timorxjava.flickr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.flickr.model.FlickrImage

class FlickrAdapter : ListAdapter<FlickrImage, FlickrVH>(FlickUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.flickr_item_vh, parent, false)
        return FlickrVH(view)
    }

    override fun onBindViewHolder(holder: FlickrVH, position: Int) {
        holder.bind(getItem(position))
    }
}