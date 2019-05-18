package com.dcservicez.a247services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.dcservicez.a247services.debugs.Debug;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Signup_sp extends AppCompatActivity {

    ArrayList<HashMap<String,String>> services=new ArrayList<>();
    Debug debug;
    Context context;
    Spinner spinner;
    EditText edt_exp,edt_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_sp);
        context=this;
        debug=new Debug(context);

        getVies();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference categories_ref = database.getReference("app_config").child("services");

        categories_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                    HashMap<String,String> service = new HashMap<>();

                    service.put(data.getKey(),data.child("title").getValue().toString());
                    services.add(service);

                }
                final int size=services.size();

                String []services_str=new String[size];
                for (int i=0;i<size;i++){
                    HashMap <String,String> service= (HashMap<String, String>) services.get(i);
                    services_str[i]=service.values().toString().replace("[","").replace("]","");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context, R.layout.spinner_item, services_str);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
               debug.print("size of services "+services_str.length);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

   public void ok_clicked(View view){

      try {
          Intent intent=new Intent();
          intent.putExtra("svrc",services.get(spinner.getSelectedItemPosition()).keySet().toString().replace("[","").replace("]",""));
          intent.putExtra("svrc_exp",edt_desc.getText().toString());
          intent.putExtra("svrc_desc",edt_exp.getText().toString());
          setResult(92333,intent);
          finish();
      }catch (Exception e){

      }

    }
    void getVies(){
        spinner=(Spinner)findViewById(R.id.spn_services);
        edt_exp=(EditText)findViewById(R.id.edt_experience);
        edt_desc=(EditText)findViewById(R.id.edt_description);

    }
}
