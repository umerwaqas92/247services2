package com.dcservicez.a247services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.dcservicez.a247services.Prefs.Prefs;
import com.google.firebase.database.FirebaseDatabase;

public class Reviw_User extends AppCompatActivity {
    Prefs prefs;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviw__user);
        prefs=new Prefs(this);

        id=getIntent().getExtras().getString("user_id");
    }

    class Review{
       public int rate;
       public String comment;

        public Review(int rate, String comnt) {
            this.rate = rate;
            this.comment = comnt;
        }
    }

    public void click_done(View view){
        try {
            int rate=(int)((RatingBar)findViewById(R.id.review_ratingbar)).getRating();
            String review=((EditText)findViewById(R.id.review_edtxt)).getText().toString();

            FirebaseDatabase.getInstance().getReference("Users").child(id).child("service").child("reviews").child(prefs.email()).setValue(new Review(rate,review));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
