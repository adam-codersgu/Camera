package com.codersguidebook.camera.ui.gallery

import android.app.Activity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.codersguidebook.camera.GalleryViewModel
import com.codersguidebook.camera.MainActivity
import com.codersguidebook.camera.Photo
import com.codersguidebook.camera.R
import com.codersguidebook.camera.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), getString(R.string.photo_deleted), Toast.LENGTH_LONG).show()
        }
    }

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

    fun showPopup(view: View, photo: Photo) {
        PopupMenu(requireActivity(), view).apply {
            inflate(R.menu.popup)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.popup_delete) {
                    deletePhoto(photo)
                }
                true
            }
            show()
        }
    }

    private fun deletePhoto(photo: Photo) {
        val intentSender = MediaStore.createDeleteRequest(requireActivity().applicationContext.contentResolver,
            listOf(photo.uri)).intentSender
        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()

        launcher.launch(intentSenderRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}