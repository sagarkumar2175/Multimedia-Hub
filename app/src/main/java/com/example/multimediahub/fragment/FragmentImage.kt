package com.example.multimediahub.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.ImageAdapter
import com.example.multimediahub.R


class FragmentImage : Fragment() {
    private lateinit var imageRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_images, container, false)
        imageRecyclerView = view.findViewById(R.id.recycler_image)
        imageRecyclerView.layoutManager = GridLayoutManager(context, 2)
        imageRecyclerView.adapter = ImageAdapter(loadImagePaths())
        return view
    }

    private fun loadImagePaths(): List<String> {
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        val imagePaths = mutableListOf<String>()
        cursor?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(columnIndex)
                imagePaths.add(imagePath)
            }
        }

        return imagePaths
    }
}
