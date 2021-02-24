package com.manoj.phonyhub;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DownloadFile extends AsyncTask<String, Integer, List<String>> {

    ProgressDialog progressDialog;
    Context context;
    String url;

    public DownloadFile(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(context, "", "Please wait, Downloading...");
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait, Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected List<String> doInBackground(String... tasks) {

        // Get the number of task
        int count = tasks.length;
        // Initialize a new list
        List<String> taskList = new ArrayList<>(count);

        // Loop through the task
        for (int i = 0; i < count; i++) {
            // Do the main task here
            String currentTask = tasks[i];
            taskList.add(currentTask);

            int counts;
            try {
                URL urls = new URL(url);
                URLConnection connection = urls.openConnection();
                connection.setDoOutput(true);
                connection.connect();

                String[] path = urls.getPath().split("/");
                String fileName = path[path.length - 1];
                //String fileExtension = file.getName().lastIndexOf("." + 1);
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                int fileSize = connection.getContentLength();

                String downloadPath = Environment.getExternalStorageDirectory() + "/my/";
                File fileDir = new File(downloadPath);
                File outputFile = new File(fileDir, fileName);
                if (!fileDir.exists()) fileDir.mkdirs();

                //InputStream input = connection.getInputStream();
                InputStream input = new BufferedInputStream(urls.openStream(), 8192);
                OutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[1024];
                long total = 0;
                while ((counts = input.read(data)) != -1) {
                    total += counts;
                    // writing data to file
                    output.write(data, 0, counts);
                }

                output.flush();
                output.close();
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Publish the async task progress & added 1 because index start from 0
            double progressPercent = (((i + 1) / (float) count) * 100.00);
            publishProgress((int) progressPercent);
            //publishProgress( (int) ( ( (i+1) / (float) count ) * 100 ) );

            // If the AsyncTask cancelled
            if (isCancelled()) {
                break;
            }

        }

        return taskList;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        for (int i = 0; i < result.size(); i++) {
            HelperMethod.toast("Download Complete");
        }
    }

}
