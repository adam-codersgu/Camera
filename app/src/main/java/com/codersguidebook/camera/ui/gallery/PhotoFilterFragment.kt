package com.codersguidebook.camera.ui.gallery

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.codersguidebook.camera.Photo
import com.codersguidebook.camera.databinding.FragmentPhotoFilterBinding

class PhotoFilterFragment : Fragment() {

    private var _binding: FragmentPhotoFilterBinding? = null
    private val binding get() = _binding!!
    private var photo: Photo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadImage(null)
    }

    private fun loadImage(glideFilter: Transformation<Bitmap>?){
        when {
            photo != null && glideFilter != null -> {
                Glide.with(this)
                    .load(photo!!.uri)
                    .transform(
                        CenterCrop(),
                        glideFilter
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.selectedImage)
            }
            photo != null -> {
                Glide.with(this)
                    .load(photo!!.uri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.selectedImage)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
