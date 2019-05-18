package com.dcservicez.a247services;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikhaellopez.circularimageview.CircularImageView;

public class Account_Managment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__managment);

        CircularImageView circularImageView = (CircularImageView)findViewById(R.id.yourCircularImageView);
// Set Border
        circularImageView.setBorderColor(getResources().getColor(R.color.your_color));
        circularImageView.setBorderWidth(1);
// Add Shadow with default param
        circularImageView.addShadow();
// or with custom param
        circularImageView.setShadowRadius(1);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
    }
}
