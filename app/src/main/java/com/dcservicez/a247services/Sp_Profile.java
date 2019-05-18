package com.dcservicez.a247services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dcservicez.a247services.Adopters.Services_Adopter;
import com.dcservicez.a247services.objects.Chat_Itm;
import com.dcservicez.a247services.objects.Review_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Sp_Profile extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView_namme,textView_srvc;


    ImageView imageView_srvc;
    CircularImageView img_profile;

    Adopter adopter;

    RatingBar ratingBar;
TextView sp_rating_txtview1;




public void abt_click(View view){
    Intent i=new Intent(this,about_sp_profile.class);
    i.putExtra("user_id",getIntent().getExtras().getString("user_id"));
    startActivity(i);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp__profile);
        Intent intent=getIntent();

        String id=intent.getExtras().getString("user_id");
        recyclerView=(RecyclerView)findViewById(R.id.review1_recylerView);
        textView_namme=(TextView)findViewById(R.id.sp_name);
        textView_srvc=(TextView)findViewById(R.id.sp_service_title);
        sp_rating_txtview1=(TextView)findViewById(R.id.sp_rating_txtview1);
        ratingBar=(RatingBar) findViewById(R.id.sp_ratingbar);

        imageView_srvc=(ImageView)findViewById(R.id.sp_service_pic);
        img_profile=(CircularImageView) findViewById(R.id.sp_image);





        FirebaseDatabase.getInstance().getReference("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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

                sp_rating_txtview1.setText(rate+"");
                ratingBar.setRating(rate);
                Log.i("SP_profile",reviewItems.size()+"got reviews");
                update_recyler_view(reviewItems);


                FirebaseDatabase.getInstance().getReference("app_config").child("services").child(dataSnapshot.child("service").child("title").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                        textView_srvc.setText(dataSnapshot2.child("title").getValue().toString());
                        Picasso.get().load(dataSnapshot2.child("img_url").getValue().toString()).into(imageView_srvc);
                        Log.i("SP_profile",dataSnapshot2.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                textView_namme.setText(dataSnapshot.child("fullname").getValue().toString().toUpperCase());
                Picasso.get().load(dataSnapshot.child("profile_url").getValue().toString()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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

    public void update_recyler_view(ArrayList<Review_item> reviewItems) {

        adopter=new Adopter(reviewItems, this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2) ;
        recyclerView.setAdapter(adopter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void btnBack_account_mgt(View view) {
        finish();
    }
    public void contact_clicked(View view) {
        String id=getIntent().getExtras().getString("user_id");
        Intent intent=new Intent(this, Chat_activity.class);
        intent.putExtra("user_id",id);
        startActivity(intent);
    }



public class Adopter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<Review_item>review_items;
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