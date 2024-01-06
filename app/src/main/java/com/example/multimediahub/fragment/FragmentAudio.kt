package com.example.multimediahub.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.MusicListAdapter
import com.example.multimediahub.AudioModal
import com.example.multimediahub.R
import java.io.File
import java.util.ArrayList

class FragmentAudio : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noMusicTextView: TextView
    private val songsList = ArrayList<AudioModal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_Audio)
        noMusicTextView = view.findViewById(R.id.no_songs_text)

        if (!checkPermission()) {
            requestPermission()
            return
        }
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val cursor = context?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val songData = AudioModal(
                    it.getString(1),
                    it.getString(0),
                    it.getString(2)
                )
                if (File(songData.path).exists())
                    songsList.add(songData)
            }
        }

        if (songsList.size == 0) {
            noMusicTextView.visibility = View.VISIBLE
        } else {
            //recyclerview
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = MusicListAdapter(songsList, requireContext())
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity() as FragmentActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            123
        )
    }

    override fun onResume() {
        super.onResume()
        if (this::recyclerView.isInitialized) {
            recyclerView.adapter = MusicListAdapter(songsList, requireContext())
        }
    }
}
