package com.dcservicez.a247services;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.Review;
import com.dcservicez.a247services.objects.Review_item;
import com.google.firebase.database.FirebaseDatabase;

public class Reviw_User extends AppCompatActivity {
    Prefs prefs;
    String id;
    boolean isSP=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviw__user);
        prefs=new Prefs(this);

        id=getIntent().getExtras().getString("user_id");
        isSP=getIntent().getExtras().getBoolean("isSP");
    }


    public void click_report(View view){
        final EditText editText= new EditText(this);
        editText.setHint("Write a report");

        new AlertDialog.Builder(this).setTitle("Report against "+id)
                .setView(editText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Reports").child(id).child(prefs.email()).setValue(editText.getText().toString());
                        Toast.makeText(Reviw_User.this,"Report has been sent successfully ",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("No",null).setCancelable(false)
                .show();

    }


    public void click_done(View view){
        try {
            int rate=(int)((RatingBar)findViewById(R.id.review_ratingbar)).getRating();
            String review=((EditText)findViewById(R.id.review_edtxt)).getText().toString();
            Review review1=new Review(rate,review);

            if(isSP){
                FirebaseDatabase.getInstance().getReference("Users").child(id).child("service").child("reviews").child(prefs.email()).setValue(review1);
            }else{
                FirebaseDatabase.getInstance().getReference("Users").child(id).child("reviews").child(prefs.email()).setValue(review1);
            }

            finish();
        } catch (Exception e) {
            e.printStackTrace();
//
//
//
        }


    }
}
