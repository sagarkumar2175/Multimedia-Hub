package com.example.multimediahub.Fragment

import android.content.ContentResolver
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.VideoAdapter
import com.example.multimediahub.Adapter.VideoListItem
import com.example.multimediahub.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VideoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var videoAdapter: VideoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        recyclerView = view.findViewById(R.id.videoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVideos()
    }

    override fun onResume() {
        super.onResume()
        loadVideos()
    }

    fun loadVideos() {
        if (!isAdded || context == null) return
        val resolver = requireContext().contentResolver

        viewLifecycleOwner.lifecycleScope.launch {
            val (items, rawVideos) = withContext(Dispatchers.IO) {
                queryVideosWithMonthGrouping(resolver)
            }
            if (isAdded && context != null) {
                videoAdapter = VideoAdapter(items, rawVideos, requireContext())
                recyclerView.adapter = videoAdapter
            }
        }
    }

    private fun queryVideosWithMonthGrouping(contentResolver: ContentResolver): Pair<List<VideoListItem>, List<VideoData>> {
        val items = mutableListOf<VideoListItem>()
        val rawVideos = mutableListOf<VideoData>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        try {
            val cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            var lastMonthGroup = ""

            cursor?.use {
                val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val titleColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                val dataColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val dateColumnIndex = it.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)

                while (it.moveToNext()) {
                    val videoId = it.getLong(idColumnIndex)
                    val videoTitle = it.getString(titleColumnIndex) ?: "Untitled"
                    val videoPath = it.getString(dataColumnIndex) ?: ""
                    val dateAddedSec = if (dateColumnIndex != -1) it.getLong(dateColumnIndex) else 0L

                    val monthGroup = if (dateAddedSec > 0) {
                        monthFormat.format(Date(dateAddedSec * 1000L))
                    } else {
                        "Earlier"
                    }

                    if (monthGroup != lastMonthGroup) {
                        items.add(VideoListItem.Header(monthGroup))
                        lastMonthGroup = monthGroup
                    }

                    val rawIndex = rawVideos.size
                    val videoData = VideoData(videoId, videoTitle, videoPath, dateAddedSec)
                    rawVideos.add(videoData)
                    items.add(VideoListItem.Video(videoData, rawIndex))
                }
            }
        } catch (e: SecurityException) {
            // Permission not yet granted
        }

        return Pair(items, rawVideos)
    }

    data class VideoData(val id: Long, val title: String, val path: String, val dateAdded: Long = 0L)
}
