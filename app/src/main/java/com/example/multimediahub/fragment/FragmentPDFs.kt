package com.example.multimediahub.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Adapter.PDFsAdapter
import com.example.multimediahub.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.util.Locale

class FragmentPDFs : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var pdFsAdapter: PDFsAdapter? = null
    private var fileList: MutableList<File>? = null
    private var pdf_icon: ImageView? = null
    private var tv_pdfs: TextView? = null
    var storage: File? = null
    var viewParam: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_p_d_fs, container, false)
        val tv_pdfs = view.findViewById<TextView>(R.id.tv_pdfs)
        val pdf_icon = view.findViewById<ImageView>(R.id.pdf_icon)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_PDFs)
        val internalStorage = System.getenv("EXTERNAL_STORAGE")
        storage = File(internalStorage)
        runtimePermission()
        return view

    }

    private fun runtimePermission() {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                displayFiles()
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
    }

    fun findFiles(file: File?): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file!!.listFiles()
        for (singleFile in files) {
            if (singleFile.isDirectory && !singleFile.isHidden) {
                arrayList.remove(singleFile)
            }
        }
        for (singleFile in files) {
            if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".pdf")) {
                arrayList.add(singleFile)
            }
        }
        return arrayList
    }

    private fun displayFiles() {
        recyclerView = requireView().findViewById(R.id.recycler_PDFs)
        recyclerView?.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1)
        }
        fileList = ArrayList()
        (fileList as ArrayList<File>).addAll(findFiles(storage))
        pdFsAdapter = PDFsAdapter(context, fileList)
        recyclerView?.run { adapter = pdFsAdapter }
    }
}



