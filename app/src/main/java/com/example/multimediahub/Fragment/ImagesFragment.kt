package com.example.multimediahub.Fragment

import android.content.ContentResolver
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.ImageAdapter
import com.example.multimediahub.R

class ImagesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_images, container, false)

        recyclerView = view.findViewById(R.id.imagesRecyclerView)
        imageAdapter = ImageAdapter(requireContext(), getImageList(requireContext().contentResolver))

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = imageAdapter

        return view
    }

    private fun getImageList(contentResolver: ContentResolver): List<String> {
        val imageList = mutableListOf<String>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val imageId = it.getLong(columnIndex)
                val imagePath = "${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}/$imageId"
                imageList.add(imagePath)
            }
        }

        return imageList
    }
}



