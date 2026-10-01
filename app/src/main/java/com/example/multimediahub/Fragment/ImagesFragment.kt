package com.example.multimediahub.Fragment

import android.content.ContentResolver
import android.content.res.Configuration
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.ImageAdapter
import com.example.multimediahub.Adapter.ImageListItem
import com.example.multimediahub.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImagesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var imageAdapter: ImageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.imagesRecyclerView)

        setupLayoutManager()
        loadImages()
    }

    private fun setupLayoutManager() {
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }

        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (imageAdapter?.isHeader(position) == true) spanCount else 1
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
    }

    override fun onResume() {
        super.onResume()
        loadImages()
    }

    fun loadImages() {
        if (!isAdded || context == null) return
        val resolver = requireContext().contentResolver

        viewLifecycleOwner.lifecycleScope.launch {
            val (items, rawUrls) = withContext(Dispatchers.IO) {
                queryImagesWithMonthGrouping(resolver)
            }
            if (isAdded && context != null) {
                imageAdapter = ImageAdapter(requireContext(), items, rawUrls)
                recyclerView.adapter = imageAdapter
            }
        }
    }

    private fun queryImagesWithMonthGrouping(contentResolver: ContentResolver): Pair<List<ImageListItem>, List<String>> {
        val items = mutableListOf<ImageListItem>()
        val rawUrls = mutableListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        try {
            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            var lastMonthGroup = ""

            cursor?.use {
                val idIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

                while (it.moveToNext()) {
                    val imageId = it.getLong(idIndex)
                    val dateAddedSec = if (dateIndex != -1) it.getLong(dateIndex) else 0L

                    val imagePath = "${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}/$imageId"
                    val monthGroup = if (dateAddedSec > 0) {
                        monthFormat.format(Date(dateAddedSec * 1000L))
                    } else {
                        "Earlier"
                    }

                    if (monthGroup != lastMonthGroup) {
                        items.add(ImageListItem.Header(monthGroup))
                        lastMonthGroup = monthGroup
                    }

                    val rawIndex = rawUrls.size
                    rawUrls.add(imagePath)
                    items.add(ImageListItem.Image(imagePath, rawIndex))
                }
            }
        } catch (e: SecurityException) {
            // Permission denied or not yet granted
        }

        return Pair(items, rawUrls)
    }
}
