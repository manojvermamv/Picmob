package com.manoj.phonyhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class DebugActivity extends Activity {

    String[] exceptionType = {
            "",
            "",
            "",
            "",
            ""

    };

    String[] errMessage = new String[]{"", "", "", "", ""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        String errMsg = "";
        String madeErrMsg = "";
        if (intent != null) {
            errMsg = intent.getStringExtra("error");

            String[] spilt = errMsg.split("\n");
            //errMsg = spilt[0];
            try {
                for (int j = 0; j < exceptionType.length; j++) {
                    if (spilt[0].contains(exceptionType[j])) {
                        madeErrMsg = errMessage[j];

                        int addIndex = spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();

                        madeErrMsg += spilt[0].substring(addIndex, spilt[0].length());
                        break;

                    }
                }

                if (madeErrMsg.isEmpty()) madeErrMsg = errMsg;
            } catch (Exception e) {
            }

        }

        //AlertDialog.Builder bld = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        AlertDialog.Builder bld = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

        bld.setTitle("An error occured");
        bld.setMessage(madeErrMsg);
        bld.setCancelable(false);

        String finalMadeErrMsg = madeErrMsg;
        bld.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HelperMethod.restartApp();
            }
        });

        bld.setNeutralButton("Copy Error", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HelperMethod.copyToClipboard(finalMadeErrMsg);
                HelperMethod.toast("Error Copied to clipboard");
            }
        });

        bld.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        bld.create().show();

    }

}
