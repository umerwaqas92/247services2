package com.dcservicez.a247services;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcservicez.a247services.Adopters.Services_Adopter;
import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.MyLocation;
import com.dcservicez.a247services.objects.Review_item;
import com.dcservicez.a247services.objects.Service;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class Customer_Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<Service> services=new ArrayList<>();
    ArrayList<Service> services_Filter=new ArrayList<>();
    Context context;
    RecyclerView recyclerView;

    EditText edt_search;
    Services_Adopter adopter;

    Prefs prefs;

    ImageButton user_btn_menu;
    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__main);
          DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        prefs=new Prefs(this);
        context=this;
        recyclerView=(RecyclerView)findViewById(R.id.service_select_recyclerView);
        edt_search=(EditText)findViewById(R.id.select_service_searrch_edt);
        user_btn_menu=(ImageButton)findViewById(R.id.user_btn_menu);


        update_location();

//        check_task_status();


        user_btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Press",Toast.LENGTH_SHORT).show();

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);


            }
        });



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


        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                TextView textView_namme=(TextView)findViewById(R.id.nav_heder_name);


                textView_namme.setText(dataSnapshot.child("fullname").getValue().toString().toUpperCase());
                Picasso.get().load(dataSnapshot.child("profile_url").getValue().toString()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        ImageView img_profile=(ImageView)findViewById(R.id.nav_heder_profilePic);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("islogin").setValue(true);
    }


    public void menu_profile_fragment(View view){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
         

    }


    public void update_location(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        // Log.e("GMAP",location.getLongitude()+"");

                        MyLocation myLocation=new MyLocation(location.getLatitude(),location.getLongitude());
                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("location").setValue(myLocation);

                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("islogin").setValue(false);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(this,Profike_Managment.class));
        } else if (id == R.id.nav_home) {

            startActivity(new Intent(this, Chat_Conversations.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void update_recyler_view() {

        adopter=new Services_Adopter(services_Filter, Customer_Main.this);
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
}
