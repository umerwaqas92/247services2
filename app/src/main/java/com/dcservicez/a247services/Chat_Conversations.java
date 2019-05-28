package com.dcservicez.a247services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dcservicez.a247services.Adopters.Chat_Adopter;
import com.dcservicez.a247services.Adopters.Conversation_Adoper;
import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.Conversation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat_Conversations extends AppCompatActivity {

    Prefs prefs;
    ArrayList<Conversation> conversations=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__conversations);
        prefs=new Prefs(this);

        final Conversation_Adoper adopter=new Conversation_Adoper(this,conversations);
        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.conversation_recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setAdapter(adopter);

        recyclerView.setLayoutManager(linearLayoutManager);


        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot data, @Nullable String s) {

                FirebaseDatabase.getInstance().getReference("Users").child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       try {
                           String name=dataSnapshot.child("fullname").getValue().toString();
                           String profile=dataSnapshot.child("profile_url").getValue().toString();
                           Conversation convr=new Conversation(data.child("info").child("last_msg").getValue().toString(),name,Long.parseLong(data.child("info").child("time").getValue().toString()),Boolean.parseBoolean(data.child("info").child("isread").getValue().toString()),profile,data.getKey());
                           conversations.add(convr);
                           adopter.notifyDataSetChanged();
                       }catch (Exception e){

                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void btnBack_account_mgt(View view) {
//        Intent intent=new Intent(Chat_Conversations.this,SP_Main_Acitvity.class);
//        startActivity(intent);
        finish();
    }
}
