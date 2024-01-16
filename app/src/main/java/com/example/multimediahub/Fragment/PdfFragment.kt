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
import com.example.multimediahub.Adapter.PdfAdapter
import com.example.multimediahub.R

class PdfFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pdfAdapter: PdfAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        recyclerView = view.findViewById(R.id.pdfRecyclerView)
        pdfAdapter = PdfAdapter(getPdfList(requireContext().contentResolver))

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pdfAdapter

        return view
    }

    private fun getPdfList(contentResolver: ContentResolver): List<Pair<String, String>> {
        val pdfList = mutableListOf<Pair<String, String>>()

        val projection = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE
        )
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("application/pdf")
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            while (it.moveToNext()) {
                val pdfPath = it.getString(dataIndex)
                val pdfTitle = it.getString(titleIndex)
                pdfList.add(Pair(pdfPath, pdfTitle))
            }
        }

        return pdfList
    }
}
