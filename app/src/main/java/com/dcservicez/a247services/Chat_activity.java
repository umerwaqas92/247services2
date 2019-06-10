package com.dcservicez.a247services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcservicez.a247services.Adopters.Chat_Adopter;
import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.Chat_Itm;
import com.dcservicez.a247services.objects.Conversation2;
import com.dcservicez.a247services.objects.Task;
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
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Chat_activity extends AppCompatActivity implements View.OnClickListener {

    final ArrayList<Chat_Itm> chat_itms=new ArrayList<>();
    String id="vovokhan@gmail,com";
    Context context;

    Prefs prefs;

    LinearLayout chat_menu_fragment;

    Button btn_hire,btn_show_map,btn_leave_conersation;
    boolean show_fragment=false;
    boolean isFirstime=true;
    boolean isFirstime2=true;

    public void View_profile_his(View view){
        Intent i=new Intent(this,Sp_Profile.class);
        i.putExtra("user_id",id);

//        i.putExtra("isSP",false);
        startActivity(i);

    }

    public void call_this_person(View view){
       FirebaseDatabase.getInstance().getReference("Users").child(id).child("mobile_no").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Uri number = Uri.parse(dataSnapshot.getValue().toString());
               Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }


    public void btn_showMap(){
        Intent i=new Intent(this,search_service.class);
        i.putExtra("user_id",id);
        startActivity(i);
//      finish();

    }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_activity);
                    prefs=new Prefs(this);
            context=this;
            id=getIntent().getExtras().getString("user_id");

           chat_menu_fragment=(LinearLayout)findViewById(R.id.chat_menu_fragment);

           //olny if you are a customer
           if(prefs.sverc_type().isEmpty())
            check_task_status();

           btn_leave_conersation=(Button)findViewById(R.id.btn_leave_conversation);
           btn_show_map=(Button)findViewById(R.id.btn_show_map);
           btn_hire=(Button)findViewById(R.id.btn_hire);

           btn_leave_conersation.setOnClickListener(this);
           btn_show_map.setOnClickListener(this);
           btn_hire.setOnClickListener(this);



            final Chat_Adopter adopter=new Chat_Adopter(chat_itms,this);
            final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_chat);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            recyclerView.setAdapter(adopter);

            recyclerView.setLayoutManager(linearLayoutManager);

            if(!prefs.sverc_type().isEmpty() ){
                ///it is customer

                btn_hire.setVisibility(View.GONE);
//                ((Button)findViewById(R.id.View_profile_his)).setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        int status=Integer.parseInt(dataSnapshot.child("status").getValue().toString());


                        if(status==0){
//                            AlertDialog alertDialog=new AlertDialog.Builder(context)
//                                    .setTitle("New Task")
//                                    .setMessage("Do you accpet this task?")
//                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(-1);//rejected
//                                        }
//                                    })
//                                    .setCancelable(false)
//                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(1);//accpted
//                                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(1);//accpted
////                                            button.setVisibility(View.VISIBLE);
//
//
//                                        }
//                                    }).create();
                            try {
                                startActivity(new Intent(context,SP_Main_Acitvity.class));
                                finish();

//                                alertDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        int status=Integer.parseInt(dataSnapshot.child("status").getValue().toString());




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
             chatItm=new Chat_Itm(msg,System.currentTimeMillis(),1);
            FirebaseDatabase.getInstance().getReference("Users").child(id).child("messages").child(prefs.email()).child(t+"").setValue(chatItm);





        }

        public void  menu_profile_fragment(final View view) {
            chat_menu_fragment.setVisibility(View.VISIBLE);
            show_fragment = true;
            return;
        }
        public void fun_hire() {
            AlertDialog alertDialog=new AlertDialog.Builder(this)
                    .setTitle("Hiring for task")
                    .setMessage("Are you sure to hire this person for task?")
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
                                .setTitle("How was the service ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(10);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(10);//accpted
                                        Intent intent=new Intent(context, Reviw_User.class);
                                        intent.putExtra("user_id",dataSnapshot.child("id").getValue().toString());
                                        intent.putExtra("isSP",true);
                                        startActivity(intent);
                                    }
                                }).create();
                        alertDialog.show();
                    }

                    if(status==6){
                        AlertDialog alertDialog=new AlertDialog.Builder(context)
                                .setMessage("Task Completion")
                                .setTitle("Is your Service Provider done his job?")
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
                                .setTitle("Is your Service Provider is arrived?")
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
                                .setMessage("Your assigned task is accepted")
                                .setTitle("Accepted")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(3);//accpted
                                        FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(3);//accpted

                                    }
                                }).create();
                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onBackPressed() {
        if (show_fragment==true){
            chat_menu_fragment.setVisibility(View.GONE);
            show_fragment=false;
        }else if (show_fragment==false){
             super.onBackPressed();



        }
    }

    @Override
    public void onClick(View v) {
        chat_menu_fragment.setVisibility(View.GONE);

        switch (v.getId()){
            case R.id.btn_hire:
                fun_hire();
                break;
            case R.id.btn_show_map:
  //              Intent intentMap=new Intent(getBaseContext(),search_service.class);
//                startActivity(intentMap);
                btn_showMap();

//                finish();
                break;
            case R.id.btn_leave_conversation:
//                Intent intentleave=new Intent(getBaseContext(),Select_service.class);
//               startActivity(intentleave);
                finish();
                break;
        }

    }
}

