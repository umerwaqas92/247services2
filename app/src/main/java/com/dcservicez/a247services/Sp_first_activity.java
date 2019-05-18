package com.dcservicez.a247services;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Sp_first_activity extends AppCompatActivity {

    LinearLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_first_activity);

        fragmentLayout=(LinearLayout) findViewById(R.id.menu_fragment);
    }

    public void menu_profile_fragment(View view) {

        fragmentLayout.setVisibility(View.VISIBLE);
    }


}
