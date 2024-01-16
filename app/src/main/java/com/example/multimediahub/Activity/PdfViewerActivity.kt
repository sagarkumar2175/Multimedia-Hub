package com.example.multimediahub.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediahub.R
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class PdfViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView: PDFView = findViewById(R.id.pdfView)
        val pdfFilePath = intent.getStringExtra(EXTRA_PDF_PATH)

        if (pdfFilePath != null) {
            displayPdf(pdfView, pdfFilePath)
        }
    }

    private fun displayPdf(pdfView: PDFView, pdfFilePath: String) {
        val file = File(pdfFilePath)
        pdfView.fromFile(file)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .load()
    }

    companion object {
        const val EXTRA_PDF_PATH = "extra_pdf_path"
    }
}