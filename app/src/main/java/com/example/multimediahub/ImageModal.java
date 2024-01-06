package com.example.multimediahub;

import android.net.Uri;

public class ImageModal {
    private Uri path;

    public ImageModal(Uri path) {
        this.path = path;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }
}
