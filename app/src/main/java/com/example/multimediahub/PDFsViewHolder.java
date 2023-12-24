package com.example.multimediahub;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PDFsViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvSize;
    public CardView container;
    public ImageView imgFile;
    public PDFsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_pdfName);
        tvSize = itemView.findViewById(R.id.tvPDFSize);
        container = itemView.findViewById(R.id.pdf_container);
        imgFile = itemView.findViewById(R.id.pdf_item);

    }
}