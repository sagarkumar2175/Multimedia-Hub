package com.example.multimediahub.fragment

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.ImageAdapter
import com.example.multimediahub.ImageModal
import com.example.multimediahub.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

import java.util.ArrayList


class FragmentImages : Fragment() {

        lateinit var recyclerView: RecyclerView
        val list = ArrayList<ImageModal>()
        lateinit var imageAdapter: ImageAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_images, container, false)
            recyclerView = view.findViewById(R.id.recycler_image)

            val layoutManager = GridLayoutManager(context, 2)
            recyclerView.layoutManager = layoutManager

            imageAdapter = ImageAdapter(list, context)
            recyclerView.adapter = imageAdapter

            Dexter.withContext(context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    @RequiresApi(Build.VERSION_CODES.Q)
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        ReadSdcard(context)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()

            return view
        }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun ReadS0dcard(context: Context?) {
            val collection: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val projection = arrayOf(MediaStore.Images.Media._ID)

            requireContext().contentResolver.query(
                collection,
                projection,
                null,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    list.add(ImageModal(contentUri))
                }
                imageAdapter.notifyDataSetChanged()
            }
        }

}