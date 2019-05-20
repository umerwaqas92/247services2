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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcservicez.a247services.Prefs.Prefs;
import com.dcservicez.a247services.objects.Review_item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class SP_Main_Acitvity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Prefs prefs;
    Context context;

    private FusedLocationProviderClient fusedLocationClient;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp__main__acitvity);
        prefs = new Prefs(this);
        context = this;
        button=(Button)findViewById(R.id.sp_main_btn);

        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                int status=Integer.parseInt(dataSnapshot.child("status").getValue().toString());


                        if(status==0){
                            AlertDialog alertDialog=new AlertDialog.Builder(context)
                                    .setTitle("New Task")
                                    .setMessage("Do you accpet this task?")
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(-1);//rejected
                                        }
                                    })
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(1);//accpted
                                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(1);//accpted
                                            button.setVisibility(View.VISIBLE);
                                        }
                                    }).create();
                            alertDialog.show();
                        }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                int status=Integer.parseInt(dataSnapshot.child("status").getValue().toString());


                if(status==7){
                    button.setVisibility(View.VISIBLE);
                    button.setText("Collected Payment");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            button.setVisibility(View.GONE);
                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(8);//accpted
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(8);//accpted

                        }
                    });}



                if(status==3){
                    button.setVisibility(View.VISIBLE);
                    button.setText("Im arrived");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(4);//accpted
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(4);//accpted

                        }
                    });





            }

                if(status==5){
                    button.setText("I have completed my task");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(6);//accpted
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("id").getValue().toString()).child("tasks").child(dataSnapshot.getKey()).child("status").setValue(6);//accpted

                        }
                    });}


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


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.e("GMAP",location.getLongitude()+"");
                        update_location(location);
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                int rate_avg=0;
                int count=0;
                final   ArrayList<Review_item> reviewItems=new ArrayList<>();

                for (final DataSnapshot review:dataSnapshot.child("service").child("reviews").getChildren()) {

                    int rate=Integer.parseInt(review.child("rate").getValue().toString());
                    count++;
                    rate_avg+=rate;

                    String url="https://firebasestorage.googleapis.com/v0/b/students24by7.appspot.com/o/aizaz_pic.jpg?alt=media&token=8e1a7e81-0dd1-42d2-bfde-6aed919b9648";
                    final   Review_item reviewItem= new Review_item(review.child("comment").getValue().toString(),rate);


                    FirebaseDatabase.getInstance().getReference("Users").child(review.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                reviewItem.setName(dataSnapshot.child("fullname").getValue().toString());
                                reviewItem.setUrl_img(dataSnapshot.child("profile_url").getValue().toString());
                            }else {
                                reviewItem.setName(review.getKey());//dataSnapshot.child("fullname").getValue().toString());

                            }
                            reviewItems.add(reviewItem);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                float rate=(float)rate_avg/count;

                TextView sp_rating_txtview1=(TextView)findViewById(R.id.sp_rating_txtview1);

                sp_rating_txtview1.setText(rate+"");
                RatingBar ratingBar=(RatingBar)findViewById(R.id.sp_ratingbar);
                ratingBar.setRating(rate);
                Log.i("SP_profile",reviewItems.size()+"got reviews");
                update_recyler_view(reviewItems);

                TextView status=(TextView)findViewById(R.id.sp_maain_status_txt);
                RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relative_sp_first);

                if(Boolean.parseBoolean(dataSnapshot.child("service").child("active").getValue().toString())){
                    status.setText("Active");
                    relativeLayout.setBackgroundResource(R.drawable.active_status_sp);
                }else {
                    relativeLayout.setBackgroundResource(R.drawable.inactive_status_sp);
                    status.setText("Inactive");
                }


                FirebaseDatabase.getInstance().getReference("app_config").child("services").child(dataSnapshot.child("service").child("title").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                        TextView textView_srvc=(TextView)findViewById(R.id.sp_service_title);
                        ImageView imageView_srvc=(ImageView) findViewById(R.id.sp_service_pic);

                        textView_srvc.setText(dataSnapshot2.child("title").getValue().toString());
                        Picasso.get().load(dataSnapshot2.child("img_url").getValue().toString()).into(imageView_srvc);
                        Log.i("SP_profile",dataSnapshot2.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("islogin").setValue(false);
    }

    public void update_location(Location location){

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child("service_provider_location").child(prefs.sverc_type()).child(prefs.email()).child("Latitude").setValue(location.getLatitude());
        ref.child("service_provider_location").child(prefs.sverc_type()).child(prefs.email()).child("Longitude").setValue(location.getLongitude());


    }


    public void update_recyler_view(ArrayList<Review_item> reviewItems) {

        Adopter adopter=new Adopter(reviewItems, this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2) ;
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.review1_recylerView);
        recyclerView.setAdapter(adopter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    public void menu_profile_fragment(View view){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_home){
            startActivity(new Intent(this,Chat_Conversations.class));
        } else if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(this,Change_profile_pic.class));


        }else  if (id == R.id.status_change) {
            FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("service").child("active").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(Boolean.parseBoolean(dataSnapshot.getValue().toString())){
                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("service").child("active").setValue(false);
                    }else{
                        FirebaseDatabase.getInstance().getReference("Users").child(prefs.email()).child("service").child("active").setValue(true);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public class Adopter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<Review_item> review_items;
        Context context;

        public Adopter(ArrayList<Review_item> review_items, Context context) {
            this.review_items = review_items;
            this.context = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view= LayoutInflater.from(context).inflate(R.layout.review_item,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Review_item reviewItem=review_items.get(position);
            holder.textView_name.setText(reviewItem.getName());
            holder.textView_comments.setText(reviewItem.getDesc());
            holder.stars.setText(reviewItem.getStars()+"");
            if(!reviewItem.getUrl_img().isEmpty())
                Picasso.get().load(reviewItem.getUrl_img()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.circularImageView.setImageBitmap(bitmap);
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
        public int getItemCount() {
            return review_items.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircularImageView circularImageView;
        TextView textView_name;
        TextView textView_comments;
        TextView stars;


        public ViewHolder(View v) {
            super(v);
            circularImageView=(CircularImageView)v.findViewById(R.id.review1_img);
            textView_name=(TextView)v.findViewById(R.id.txtview_review_name);
            textView_comments=(TextView)v.findViewById(R.id.txtview_review_description);
            stars=(TextView)v.findViewById(R.id.review1_ratingTxt);
        }

    }
}
