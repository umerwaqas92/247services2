package com.dcservicez.a247services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import com.dcservicez.a247services.Adopters.Services_Adopter;
import com.dcservicez.a247services.objects.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Select_service extends AppCompatActivity {
    ArrayList<Service> services=new ArrayList<>();
    ArrayList<Service> services_Filter=new ArrayList<>();
    Context context;
    RecyclerView recyclerView;

    String id="aizaz ahmad";
    EditText edt_search;
    Services_Adopter adopter;
    ImageButton btn_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        context=this;
        recyclerView=(RecyclerView)findViewById(R.id.service_select_recyclerView);
        edt_search=(EditText)findViewById(R.id.select_service_searrch_edt);

        id=getIntent().getExtras().getString("user");

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    search_service(charSequence.toString());
                    adopter.notifyDataSetChanged();
                }else {
                    services_Filter.clear();
                    services_Filter.addAll(services);
                    try {
                        adopter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        FirebaseDatabase.getInstance().getReference("app_config").child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                        Service service=new Service(data.child("title").getValue().toString(),data.child("img_url").getValue().toString(),Integer.parseInt(data.child("color").getValue().toString()),data.getKey().toString());
                        services.add(service);
                        services_Filter.add(service);
                }
                update_recyler_view();


                Log.i("service24/7_Adopter","got services"+services.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("service24/7_Adopter","got services"+databaseError.toString());
            }
        });

    }

    public void update_recyler_view() {

        adopter=new Services_Adopter(services_Filter, Select_service.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,2) ;
        recyclerView.setAdapter(adopter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void search_service(String q){
        services_Filter.clear();
        for (Service s:services) {
            if(s.getTitle().toLowerCase().contains(q.toLowerCase())){
                services_Filter.add(s);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
