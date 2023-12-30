package com.example.multimediahub.fragment

import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.PDFsAdapter
import com.example.multimediahub.R
import java.util.ArrayList

class FragmentPDFs : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_p_d_fs, container, false)
        recyclerView = view.findViewById(R.id.pdf_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PDFsAdapter(requireContext(), pdffiles())
        return view
    }


    private fun cursorToList(cursor: Cursor?, proj: Array<String>): List<String> {
        val pdfFiles = ArrayList<String>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val path = cursor.getString(index)
                pdfFiles.add(path)
            }
            cursor.close() // Close the cursor after fetching data
        }
        return pdfFiles
    }
    private fun pdffiles(): List<String> {
        val contentResolver = requireContext().contentResolver
        val mime = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val args = arrayOf(mimeType)
        val proj = arrayOf(MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.DISPLAY_NAME)
        val cursor = contentResolver.query(MediaStore.Files.getContentUri("external"),
            proj,mime,args,null)
        return cursorToList(cursor, proj)
    }

}