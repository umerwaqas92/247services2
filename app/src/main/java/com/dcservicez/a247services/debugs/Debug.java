package com.dcservicez.a247services.debugs;

import android.content.Context;
import android.widget.Toast;

public class Debug {
    private Context context;
    boolean isDebug=true;

    public Debug(Context context) {
        this.context = context;
    }

    public void print(String s){
        if(isDebug)
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
