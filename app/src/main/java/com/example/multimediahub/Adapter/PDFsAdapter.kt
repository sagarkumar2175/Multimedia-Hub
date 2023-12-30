package com.example.multimediahub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.R
import java.io.File

class PDFsAdapter(private val context: Context, private val pdffiles: List<String>) : RecyclerView.Adapter<PDFsAdapter.PDFsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDFsViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.imageitem, parent, false)
        return PDFsViewHolder(view)
    }


    override fun onBindViewHolder(holder: PDFsViewHolder, position: Int) {
        val path = pdffiles[position]
        val pdfFile = File(path)
        val fileName = pdfFile.name
        holder.bind(fileName)
    }
    override fun getItemCount(): Int {
        return pdffiles.size
    }

    class PDFsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val filename: TextView = itemView.findViewById(R.id.textPdfName)

        fun bind(fileName: String) {
            filename.text = fileName
        }
    }
}