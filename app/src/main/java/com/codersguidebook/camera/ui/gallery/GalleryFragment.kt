package com.codersguidebook.camera.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.codersguidebook.camera.GalleryViewModel
import com.codersguidebook.camera.MainActivity
import com.codersguidebook.camera.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: GalleryAdapter
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GalleryAdapter(activity as MainActivity, this)
        binding.root.layoutManager = GridLayoutManager(context, 3)
        binding.root.adapter = adapter

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            adapter.notifyItemRangeRemoved(0, adapter.itemCount)
            adapter.photos = photos
            adapter.notifyItemRangeInserted(0, photos.size)
        }

        if (MainActivity.CameraPermissionHelper.hasStoragePermission(requireActivity())) viewModel.loadPhotos()
        else MainActivity.CameraPermissionHelper.requestPermissions(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}