package com.dcservicez.a247services.Adopters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcservicez.a247services.R;
import com.dcservicez.a247services.Select_service;
import com.dcservicez.a247services.objects.Service;
import com.dcservicez.a247services.search_service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Services_Adopter extends  RecyclerView.Adapter<Services_Adopter.ViewHolder>  {

        ArrayList<Service> services;
        Context context;


    public Services_Adopter(ArrayList<Service> services, Context context) {

        this.services = services;
        this.context = context;
        Log.e("Select service","Passed adofter");
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(context).inflate(R.layout.service_item,parent,false);
        Log.e("Select service","Passed adofter 1");
       // Log.i("service24/7_Adopter","created");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("Select service","Passed adofter 2");
        final Service service=services.get(position);
        holder.title.setText(service.getTitle());
        Picasso.get().load(service.getImg_url()).into(holder.imageView);


        if(service.getColor()==1){
            holder.linearLayout.setBackgroundResource(R.drawable.select_service1);
//            holder.title.setTextColor(Color.parseColor("#fff1f5fe"));
        }else if(service.getColor()==2){
            holder.linearLayout.setBackgroundResource(R.drawable.select_service2);
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#fff1f5fe"));
        }else if(service.getColor()==3){
            holder.linearLayout.setBackgroundResource(R.drawable.select_service3);
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#fff1f5fe"));
        }else if(service.getColor()==4){
            holder.linearLayout.setBackgroundResource(R.drawable.select_service_4);
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#fff1f5fe"));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, search_service.class);
                i.putExtra("service_type",service.getService());
                context.startActivity(i);

//                Log.i("service24/7_Adopter","select service"+service.getService());
            }
        });


//        Log.i("service24/7_Adopter","bind"+position);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.service_item_text);
            imageView = (ImageView) v.findViewById(R.id.service_item_img);
            linearLayout=(LinearLayout) v.findViewById(R.id.service_item_linerlayout);

        }
    }
}
