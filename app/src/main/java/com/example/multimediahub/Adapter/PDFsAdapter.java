package com.example.multimediahub.Adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multimediahub.PDFsViewHolder;
import com.example.multimediahub.R;

import java.io.File;
import java.util.List;

public class PDFsAdapter extends RecyclerView.Adapter<PDFsViewHolder> {
    private Context context;
    private List<File> file;
    public PDFsAdapter(Context context, List<File> file){
        this.context = context;
        this.file = file ;
    }

    @NonNull
    @Override
    public PDFsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PDFsViewHolder(LayoutInflater.from(context).inflate(R.layout.pdfsitem, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull PDFsViewHolder holder, int position) {
        holder.tvName.setText(file.get(position).getName());
        holder.tvName.setSelected(true);
        holder.tvSize.setText(Formatter.formatShortFileSize(context, file.get(position).length()));

        if (file.get(position).getName().toLowerCase().endsWith(".pdf")) {
            holder.imgFile.setImageResource(R.drawable.baseline_picture_as_pdf_24);
        }
        else{
            holder.imgFile.setImageResource(R.drawable.baseline_picture_as_pdf_24);
        }
        
    }

    @Override
    public int getItemCount() {
        return file.size();
    }
}

