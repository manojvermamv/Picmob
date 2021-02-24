package com.manoj.phonyhub;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Config {

    public static String downloadPath = Environment.getExternalStorageDirectory() + "/my/";
    public static String currentTimeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

    public static String getFullFileName(String path) {
        String fileName = HelperMethod.getFileName(path);
        if (fileName == null) {
            fileName = "Wallpaper-" + currentTimeStamp;
        }
        if (HelperMethod.getFileExtension(fileName) == null) {
            fileName = fileName + ".jpg";
        }
        return fileName;
    }

    public static void shareWallpaper(Context context, String download_url) {
        String fullFilePath = downloadPath + getFullFileName(download_url);
        File file = new File(fullFilePath);
        if (!file.exists()) {
            new DownloadFileBackground(context, download_url).execute();
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, fileUri);
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }

    public static void setWallpaper(Context context, String download_url) {
        WallpaperManager wallpaper = WallpaperManager.getInstance(context.getApplicationContext());
        String fullFilePath = downloadPath + getFullFileName(download_url);
        // File fileDir = new File(fullFilePath);
        File file = new File(fullFilePath);
        if (!file.exists()) {
            new DownloadFileBackground(context, download_url).execute();
        }
        Bitmap bitmap = FileUtil.getScaledBitmap(fullFilePath, 100);
        if (bitmap != null) {
            try {
                wallpaper.setBitmap(bitmap);
                //wallpaper.setBitmap(HelperMethod.drawableToBitmap(dialogContext.getDrawable(R.id.picsum_full_image)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
