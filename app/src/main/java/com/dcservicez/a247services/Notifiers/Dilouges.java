package com.dcservicez.a247services.Notifiers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dilouges {
    private Context context;

    public Dilouges(Context context) {
        this.context = context;
    }

    public AlertDialog complition_dilouge(String title, String msg, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
         .setPositiveButton("Ok",listener);

        return builder.create();
    }

    public AlertDialog prograss(String title, String msg, DialogInterface.OnClickListener listener){
        ProgressDialog.Builder builder=new ProgressDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false);

        return builder.create();
    }
}
