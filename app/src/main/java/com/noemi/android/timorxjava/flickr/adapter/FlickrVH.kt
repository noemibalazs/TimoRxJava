package com.noemi.android.timorxjava.flickr.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.flickr.model.FlickrImage
import kotlinx.android.synthetic.main.flickr_item_vh.view.*

class FlickrVH(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(image: FlickrImage) {
        with(itemView) {
            Glide.with(context).load(image.thumbnail)
                .placeholder(R.drawable.tiger)
                .error(R.drawable.tiger)
                .into(ivPhoto)
            tvOwner.text = image.owner
            tvTitle.text = image.title
        }
    }
}