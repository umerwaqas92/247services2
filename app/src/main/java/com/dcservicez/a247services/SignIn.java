package com.dcservicez.a247services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dcservicez.a247services.Notifiers.Dilouges;
import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.debugs.Debug;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignIn extends AppCompatActivity {

    EditText edt_email,edt_pass;
    Dilouges dilouges;
    Context context;
    Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getViews();
        context=this;
        dilouges=new Dilouges(context);
        prefs=new Prefs(context);

        edt_email.setText(prefs.email());


        requestForSpecificPermission();
//
//        startActivity(new Intent(this, Chat_Conversations.class));
//        Intent intent=new Intent(context,Reviw_User.class);
//        intent.putExtra("user_id","demoapp9233311@gmail,com");
//        startActivity(intent);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(3)
//    public void requestLocationPermission() {
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
//        if(EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            EasyPermissions.requestPermissions(this, "Please grant the location permission", 3, perms);
//        }
//    }


    void getViews(){
        edt_email=(EditText)findViewById(R.id.edt_signin_email);
        edt_pass=(EditText)findViewById(R.id.edt_signin_password);
    }

    //coments added

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length < 0) {
            finish();
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                grantResults[2] == PackageManager.PERMISSION_GRANTED
        ) {

        } else {

            finish();
            return;

        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
//                , android.Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS
//                ,android.Manifest.permission.ACCESS_FINE_LOCATION
        }, 101);
    }


    public void redirect_signUp(View view) {
        Intent intent=new Intent(SignIn.this,MainActivity.class);
        startActivity(intent);
//        finish();
    }

    public void login(final  View view){
      final AlertDialog d= (AlertDialog) dilouges.prograss("Login..","wait..",null);
       d.show();
       view.setEnabled(false);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        String email =  edt_email.getText().toString().replace(".", ","); // firebaseDatabase.push().getKey();
        prefs.email(email);
        databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                d.hide();

                if(edt_pass.getText().toString().equals(dataSnapshot.child("password").getValue().toString())){
                    new Debug(context).print("Login success");
                    try {
                        prefs.sverc_type(dataSnapshot.child("service").child("title").getValue().toString());
                        new Debug(context).print("you are sp"+dataSnapshot.child("service").getValue().toString());
                        startActivity(new Intent(context,SP_Main_Acitvity.class));
                        finish();

                    } catch (Exception e) {
                      prefs.sverc_type("");
                        new Debug(context).print("not  sp");
                        startActivity(new Intent(context,Customer_Main.class));
                        finish();

                    }


                }else {
                    new Debug(context).print("Login not success");
                    view.setEnabled(true);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
