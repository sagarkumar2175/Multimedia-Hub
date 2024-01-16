package com.example.multimediahub.Fragment

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Activity.MusicPlayerActivity
import com.example.multimediahub.Adapter.MusicAdapter
import com.example.multimediahub.R

class MusicFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        recyclerView = view.findViewById(R.id.musicRecyclerView)
        musicAdapter = MusicAdapter(getMusicList(requireContext().contentResolver)) { metadata ->
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("MUSIC_URI", metadata.data)
            intent.putExtra("SONG_TITLE", metadata.title)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = musicAdapter

        return view
    }

    private fun getMusicList(contentResolver: ContentResolver): List<MusicAdapter.MediaMetadata> {
        val musicList = mutableListOf<MusicAdapter.MediaMetadata>()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val data = it.getString(dataIndex)
                val duration = it.getLong(durationIndex)

                musicList.add(MusicAdapter.MediaMetadata(title, data, duration))
            }
        }

        return musicList
    }

}