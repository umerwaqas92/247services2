package com.dcservicez.a247services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dcservicez.a247services.Prefs.Prefs;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class Change_profile_pic extends AppCompatActivity {

    Prefs prefs;
    FirebaseDatabase firebaseDatabase;


    public void change_profile_image(View view){

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    public void btnBack_account_mgt(View view){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_pic);
        prefs=new Prefs(this);
        firebaseDatabase=FirebaseDatabase.getInstance();



       firebaseDatabase.getReference("Users").child(prefs.email()).child("profile_url").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Picasso.get().load(dataSnapshot.getValue().toString()).into(new Target() {
                   @Override
                   public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                       CircularImageView imageView = (CircularImageView) findViewById(R.id.CircularImageView);
                    imageView.setImageBitmap(bitmap);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                FirebaseStorage storage = FirebaseStorage.getInstance();

                final StorageReference mountainsRef = storage.getReference().child("user_profile").child(prefs.email()+".jpg");

                UploadTask uploadTask =mountainsRef.putFile(uri);

                ProgressDialog.Builder builder=new AlertDialog.Builder(Change_profile_pic.this)
                        .setTitle("Wait..")
                        .setMessage("Picture is uploading")
                        .setCancelable(false);

             final  AlertDialog alertDialog= builder.show();
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downUri = task.getResult();
                            alertDialog.hide();
                            firebaseDatabase.getReference("Users").child(prefs.email()).child("profile_url").setValue(downUri.toString());

                            Log.d("GMAP", "onComplete: Url: "+ downUri.toString());


                        }
                    }
                });

//               uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                   @Override
//                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                       Log.d("GMAP", "Uploaded"+task.toString());
//
//                    alertDialog.hide();
////                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("profile_url").setValue(download);
//
//                   }
//               });
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//
//
//                CircularImageView imageView = (CircularImageView) findViewById(R.id.CircularImageView);
//                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
