package com.dcservicez.a247services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcservicez.a247services.Adopters.Chat_Adopter;
import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.Chat_Itm;
import com.dcservicez.a247services.objects.Conversation2;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import android.view.View;
import android.widget.LinearLayout;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Chat_activity extends AppCompatActivity {

    final ArrayList<Chat_Itm> chat_itms=new ArrayList<>();
    String id="vovokhan@gmail,com";
    Context context;

    Prefs prefs;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_activity);
                    prefs=new Prefs(this);
            context=this;
            id=getIntent().getExtras().getString("user_id");

//            chat_itms.add(new Chat_Itm("helloo",5454,0));
//            chat_itms.add(new Chat_Itm("??",5454,0));
//            chat_itms.add(new Chat_Itm("yes",5454,1));



            check_task_status();

            final Chat_Adopter adopter=new Chat_Adopter(chat_itms,this);
            final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_chat);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            recyclerView.setAdapter(adopter);

            recyclerView.setLayoutManager(linearLayoutManager);


            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").child(id).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot data, @Nullable String s) {


                    try {
//                          Thread.sleep(1000);
                        Chat_Itm chatItm=new Chat_Itm(data.child("msg").getValue().toString(),System.currentTimeMillis(),Integer.parseInt(data.child("msg_type").getValue().toString()));
                        chat_itms.add(chatItm);
                        adopter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chat_itms.size()-1);
                    }catch (Exception e){
//                          Log.e("chat",e.toString());
                    }
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

            FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    TextView textView_namme=(TextView)findViewById(R.id.chat_txt_name);
//                    chat_itms.clear();
//                for (DataSnapshot data:dataSnapshot.child("messages").child(id).getChildren()) {
//
//                        Chat_Itm chatItm=new Chat_Itm(data.child("msg").getValue().toString(),System.currentTimeMillis(),Integer.parseInt(data.child("msg_type").getValue().toString()));
//                        chat_itms.add(chatItm);
//
//                }
//                adopter.notifyDataSetChanged();

                    textView_namme.setText(dataSnapshot.child("fullname").getValue().toString().toUpperCase());
                    Picasso.get().load(dataSnapshot.child("profile_url").getValue().toString()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            ImageView img_profile=(ImageView)findViewById(R.id.sp_image);

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

        public void sent_sms(View view){
            EditText editText=(EditText)findViewById(R.id.edt_type_message) ;
            if(!editText.getText().toString().isEmpty()){
                sent_sms(id,editText.getText().toString(),view,editText);
//            editText.setEnabled(false);
//            view.setEnabled(false);
            }

        }

        public void sent_sms(String id,String msg,View view,EditText view1){
            long t= System.currentTimeMillis();

            view1.setEnabled(true);
            view.setEnabled(true);
            view1.setText("");
            Conversation2 convrs=new Conversation2(msg);
            FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child("info").setValue(convrs);
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").child(id).child("info").setValue(convrs);
            Chat_Itm chatItm=new Chat_Itm(msg,System.currentTimeMillis(),0);
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").child(id).child(t+"").setValue(chatItm);
//        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").child(id).child(t+"").child("msg_type").setValue(0);
//        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("messages").child(id).child(t+"").child("msg_time").setValue(ServerValue.TIMESTAMP);
            chatItm=new Chat_Itm(msg,System.currentTimeMillis(),1);
            FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child(t+"").setValue(chatItm);




//        FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child(t+"").child("msg").setValue(msg);
//        FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child(t+"").child("msg_type").setValue(1);
//        FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child(t+"").child("msg_time").setValue(ServerValue.TIMESTAMP);


        }

        public void  menu_profile_fragment(final View view){
            AlertDialog alertDialog=new AlertDialog.Builder(this)
                    .setTitle("Hireing for task")
                    .setMessage("Are you sure want to hire this person for task?")
                    .setNegativeButton("No",null)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            long t=System.currentTimeMillis();

                            FirebaseDatabase.getInstance().getReference("Users").child(id).child("tasks").child(""+t).setValue(new Task(prefs.email()));
                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(""+t).setValue(new Task(id));

                        }
                    }).create();

            alertDialog.show();
        }



        class Task {
            public long time=System.currentTimeMillis();
            public  String id=prefs.email();
            public   int status=0;
//0 new 1 accpeted 2 rejected 3 arrived 4 completed

            public Task(String id) {
                this.id = id;
            }
        }

        public void check_task_status() {
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                    int status=Integer.parseInt(dataSnapshot.child("status").getValue().toString());

                    if(status==8){
                        AlertDialog alertDialog=new AlertDialog.Builder(context)
                                .setMessage("Rate the user")
                                .setTitle("How was the service")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(10);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(10);//accpted
                                        Intent intent=new Intent(context, Reviw_User.class);
                                        intent.putExtra("user_id",dataSnapshot.child("id").getValue().toString());
                                        startActivity(intent);
                                    }
                                }).create();
                        alertDialog.show();
                    }

                    if(status==6){
                        AlertDialog alertDialog=new AlertDialog.Builder(context)
                                .setMessage("Task Complition")
                                .setTitle("is your service provider done his jobe ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(7);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(7);//accpted

                                    }
                                }).create();
                        alertDialog.show();
                    }

                    if(status==4){
                        AlertDialog alertDialog=new AlertDialog.Builder(context)
                                .setMessage("Arrived")
                                .setTitle("is your service provider is arrived?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(5);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(5);//accpted

                                    }
                                }).create();
                        alertDialog.show();
                    }
                    if(status==1){
                        AlertDialog alertDialog=new AlertDialog.Builder(context)
                                .setMessage("Your assigned task is accpted")
                                .setTitle("Accpted")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(3);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(3);//accpted

                                    }
                                }).create();
                        alertDialog.show();
                    }
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





    }




