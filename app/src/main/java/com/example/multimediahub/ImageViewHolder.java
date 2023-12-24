package com.example.multimediahub;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvSize;
    public CardView container;
    public ImageView imgfile;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_fileName);
        tvSize = itemView.findViewById(R.id.tvFileSize);
        container = itemView.findViewById(R.id.img_container);
        imgfile = itemView.findViewById(R.id.image_item);

    }
}
