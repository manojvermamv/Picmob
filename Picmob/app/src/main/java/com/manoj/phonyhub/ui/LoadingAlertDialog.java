package com.manoj.phonyhub.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.manoj.phonyhub.R;

public class LoadingAlertDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingAlertDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_progress_alert_dialog, null);

        builder.setView(view);
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void stopLoading() {
        alertDialog.dismiss();
    }

}
