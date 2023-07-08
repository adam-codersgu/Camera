package com.codersguidebook.camera.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codersguidebook.camera.MainActivity
import com.codersguidebook.camera.Photo
import com.codersguidebook.camera.R

class GalleryAdapter(private val activity: MainActivity, private val fragment: GalleryFragment):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var photos = listOf<Photo>()

    inner class GalleryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var image = itemView.findViewById<View>(R.id.image) as ImageView

        init {
            itemView.isClickable = true
            itemView.setOnClickListener {
                val action = GalleryFragmentDirections.actionPhotoFilter(photos[layoutPosition])
                it.findNavController().navigate(action)
            }

            itemView.setOnLongClickListener {
                fragment.showPopup(it, photos[layoutPosition])
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GalleryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_preview, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as GalleryViewHolder
        val current = photos[position]

        Glide.with(activity)
            .load(current.uri)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}
