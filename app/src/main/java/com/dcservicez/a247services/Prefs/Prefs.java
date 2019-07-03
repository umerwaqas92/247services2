package com.dcservicez.a247services.Prefs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Prefs {
    private Context context;
    SharedPreferences sharedPref;

    public Prefs(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

    }

    public void sverc_type(String s){
        sharedPref.edit().putString("sverc_type",s).apply();
    }

    public void task_id(String s){
        sharedPref.edit().putString("task_id",s).apply();
    }

    public String sverc_type(){
        return  sharedPref.getString("sverc_type","");
    }
    public String task_id(){
        return  sharedPref.getString("task_id","");
    }

    public void email(String s){
        sharedPref.edit().putString("email",s).apply();
    }
    public String email(){
        return  sharedPref.getString("email","");
    }

    public boolean islogin(){
        return  sharedPref.getBoolean("islogin",false);
    }

    public void setLogin(boolean login){
          sharedPref.edit().putBoolean("islogin",login).apply();
        Toast.makeText(context,islogin()+"",Toast.LENGTH_SHORT).show();

    }



}
