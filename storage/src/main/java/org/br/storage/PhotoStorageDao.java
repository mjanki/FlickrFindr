package org.br.storage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class PhotoStorageDao {
    private Context ctx;
    public PhotoStorageDao(Context ctx) {
        this.ctx = ctx;
    }

    public @Nullable Bitmap readPhotoBitmap(@NonNull String path) {
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public @NonNull String saveThumbBitmap(@NonNull String photoId, @NonNull Bitmap photoBitmap) {
        return saveBitmap(photoBitmap, photoId + "-thumb.png");
    }

    public @NonNull String saveOriginalBitmap(@NonNull String photoId, @NonNull Bitmap photoBitmap) {
        return saveBitmap(photoBitmap, photoId + "-original.png");
    }

    private @NonNull String saveBitmap(@NonNull Bitmap photoBitmap, @NonNull String name) {
        ContextWrapper ctxWrapper = new ContextWrapper(ctx);
        File directory = ctxWrapper.getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, name);

        if (file.exists()) {
            file.delete();
        }

        String path = "";

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();

            path = file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }
}
