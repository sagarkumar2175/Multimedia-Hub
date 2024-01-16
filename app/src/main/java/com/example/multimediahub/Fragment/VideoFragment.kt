package com.example.multimediahub.Fragment

import android.content.ContentResolver
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.VideoAdapter
import com.example.multimediahub.R

class VideoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        recyclerView = view.findViewById(R.id.videoRecyclerView)
        videoAdapter = VideoAdapter(getVideoList(requireContext().contentResolver),requireContext() )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = videoAdapter

        return view
    }

    private fun getVideoList(contentResolver: ContentResolver): List<VideoData> {
        val videoList = mutableListOf<VideoData>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA
        )

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val dataColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (it.moveToNext()) {
                val videoId = it.getLong(idColumnIndex)
                val videoTitle = it.getString(titleColumnIndex)
                val videoPath = it.getString(dataColumnIndex)
                videoList.add(VideoData(videoId, videoTitle, videoPath))
            }
        }

        return videoList
    }

    data class VideoData(val id: Long, val title: String, val path: String)
}
