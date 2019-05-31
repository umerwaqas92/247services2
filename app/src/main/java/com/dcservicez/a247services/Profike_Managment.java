package com.dcservicez.a247services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dcservicez.a247services.Prefs.Prefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Profike_Managment extends AppCompatActivity {
    EditText name,phone,pass,exp,desc;
    Prefs prefs;
    boolean isSP=false;
    TextView textView_exp,textView_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profike__managment);
        prefs=new Prefs(this);

        textView_exp=(TextView)findViewById(R.id.textView_experience);
        textView_desc=(TextView)findViewById(R.id.textView_Description);

        name=(EditText)findViewById(R.id.profile_manage_name_edt);
        phone=(EditText)findViewById(R.id.profile_manage_phone_edt);
        pass=(EditText)findViewById(R.id.profile_manage_pass_edt);
        exp=(EditText)findViewById(R.id.profile_manage_experince_edt);
        desc=(EditText)findViewById(R.id.profile_manage_desc_edt);
        if(prefs.sverc_type().isEmpty()){
            //customer
            isSP=false;
            textView_exp.setVisibility(View.GONE);
            textView_desc.setVisibility(View.GONE);
            exp.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }else{
            isSP=true;
        }

        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("fullname").getValue().toString());
                phone.setText(dataSnapshot.child("mobile_no").getValue().toString());
                pass.setText(dataSnapshot.child("password").getValue().toString());
                if(isSP){
                    desc.setText(dataSnapshot.child("service").child("service_des").getValue().toString());
                    exp.setText(dataSnapshot.child("service").child("service_exp").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void change_profile_image(View view){
        startActivity(new Intent(this,Change_profile_pic.class));

    }

    public void done_click(View veiw){
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("fullname").setValue(name.getText().toString());
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("mobile_no").setValue(phone.getText().toString());
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("password").setValue(pass.getText().toString());
        if (isSP) {
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("service").child("service_des").setValue(desc.getText().toString());
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("service").child("service_exp").setValue(exp.getText().toString());

        }

        finish();

    }
}
