package com.manoj.phonyhub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.manoj.phonyhub.fragments.FullscreenDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileBackground extends AsyncTask<Void, Void, String> {

    ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    Context context;
    String downloadUrl;
    File outputFile;

    public DownloadFileBackground(Context context, String url) {
        this.context = context;
        this.downloadUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait, We are working on it ..");
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        progressDialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_loading_eclipse_200px));
        progressDialog.show();

    }

    @Override
    protected String doInBackground(Void... tasks) {
        int count;
        try {
            URL urls = new URL(downloadUrl);
            URLConnection connection = urls.openConnection();
            connection.setDoOutput(true);
            connection.connect();
            int fileSize = connection.getContentLength();

            File fileDir = new File(Config.downloadPath);
            outputFile = new File(fileDir, Config.getFullFileName(downloadUrl));
            if (!fileDir.exists()) fileDir.mkdirs();

            //InputStream input = connection.getInputStream();
            InputStream input = new BufferedInputStream(urls.openStream(), 8192);
            OutputStream output = new FileOutputStream(outputFile);

            byte[] data = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // writing data to file
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Done";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.equals("Done")) {
            Message msg = new Message();
            //progressDialog.setDismissMessage(msg.);
            progressDialog.dismiss();
            HelperMethod.toastLong("File successfully saved at " + outputFile.getAbsolutePath());
        }
    }

}
