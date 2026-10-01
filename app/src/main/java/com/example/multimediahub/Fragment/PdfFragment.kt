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
import com.example.multimediahub.Adapter.PdfAdapter
import com.example.multimediahub.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PdfFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var pdfAdapter: PdfAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)
        recyclerView = view.findViewById(R.id.pdfRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPdfs()
    }

    override fun onResume() {
        super.onResume()
        loadPdfs()
    }

    fun loadPdfs() {
        if (!isAdded || context == null) return
        val resolver = requireContext().contentResolver

        viewLifecycleOwner.lifecycleScope.launch {
            val pdfList = withContext(Dispatchers.IO) {
                getPdfList(resolver)
            }
            if (isAdded && context != null) {
                pdfAdapter = PdfAdapter(pdfList)
                recyclerView.adapter = pdfAdapter
            }
        }
    }

    private fun getPdfList(contentResolver: ContentResolver): List<Pair<String, String>> {
        val pdfList = mutableListOf<Pair<String, String>>()

        val projection = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE
        )
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("application/pdf")

        try {
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
                    if (pdfPath != null) {
                        pdfList.add(Pair(pdfPath, pdfTitle ?: "Document"))
                    }
                }
            }
        } catch (e: SecurityException) {
            // Permission not yet granted
        }

        return pdfList
    }
}
