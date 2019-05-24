package com.dcservicez.a247services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dcservicez.a247services.objects.Review_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class about_sp_profile extends AppCompatActivity {

    ImageView imageView_srvc;
    CircularImageView img_profile;
    TextView textView_namme,textView_srvc;

    Sp_Profile.Adopter adopter;

    RatingBar ratingBar;
    TextView sp_rating_txtview1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sp_profile);
        String id=getIntent().getExtras().getString("user_id");


        textView_namme=(TextView)findViewById(R.id.txt_acivity_sp_name);
        textView_srvc=(TextView)findViewById(R.id.txt_acivity_sp_servc);

        img_profile=(CircularImageView) findViewById(R.id.sp_image);





        FirebaseDatabase.getInstance().getReference("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                ((TextView)findViewById(R.id.txt_acivity_sp_exp)).setText(dataSnapshot.child("service").child("service_exp").getValue().toString());
                ((TextView)findViewById(R.id.txt_acivity_sp_desc)).setText(dataSnapshot.child("service").child("service_des").getValue().toString());

                FirebaseDatabase.getInstance().getReference("app_config").child("services").child(dataSnapshot.child("service").child("title").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                        textView_srvc.setText(dataSnapshot2.child("title").getValue().toString());
//                        Picasso.get().load(dataSnapshot2.child("img_url").getValue().toString()).into(imageView_srvc);
                        Log.i("SP_profile",dataSnapshot2.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                textView_namme.setText(dataSnapshot.child("fullname").getValue().toString().toUpperCase());
                Picasso.get().load(dataSnapshot.child("profile_url").getValue().toString()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        img_profile.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void btnBack_account_mgt(View view) {
        Intent intent=new Intent(about_sp_profile.this,Sp_Profile.class);
        startActivity(intent);
        finish();
    }
}
