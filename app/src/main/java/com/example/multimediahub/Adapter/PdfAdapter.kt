package com.example.multimediahub.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Activity.PdfViewerActivity
import com.example.multimediahub.R

class PdfAdapter(private val pdfList: List<Pair<String, String>>) :
    RecyclerView.Adapter<PdfAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfTextView: TextView = itemView.findViewById(R.id.pdfTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (pdfPath, pdfTitle) = pdfList[position]
        holder.pdfTextView.text = pdfTitle

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PdfViewerActivity::class.java)
            intent.putExtra(PdfViewerActivity.EXTRA_PDF_PATH, pdfPath)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }
}
