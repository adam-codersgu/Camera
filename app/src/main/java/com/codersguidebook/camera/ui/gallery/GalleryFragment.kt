package com.codersguidebook.camera.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codersguidebook.camera.GalleryViewModel
import com.codersguidebook.camera.MainActivity
import com.codersguidebook.camera.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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

        // TODO: Apply the adapter to the RecyclerView

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            photos?.let {
                // TODO: Load the photo previews here
            }
        }

        if (MainActivity.CameraPermissionHelper.hasStoragePermission(requireActivity())) viewModel.loadPhotos()
        else MainActivity.CameraPermissionHelper.requestPermissions(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}