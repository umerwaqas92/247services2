package com.dcservicez.a247services.Adopters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcservicez.a247services.Chat_activity;
import com.dcservicez.a247services.R;
import com.dcservicez.a247services.objects.Conversation;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class Conversation_Adoper extends RecyclerView.Adapter<Conversation_Adoper.ViewHolder> {

    Context context;
    ArrayList<Conversation> conversations;


    public Conversation_Adoper(Context context, ArrayList<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.conversation_itm,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
             final    Conversation conversation=conversations.get(position);
                holder.time.setText(conversation.getTime()+"");
                holder.msg.setText(conversation.getLast_msg()+"");
                holder.name.setText(conversation.getId());
        Picasso.get().load(conversation.getUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Chat_activity.class);
                //intent.putExtra("user_id",conversation.getUsr_id());
                intent.putExtra("sp_press",conversation.getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,msg,time;
        CircularImageView imageView;
        RelativeLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            msg = (TextView) v.findViewById(R.id.conver_msg);
            name = (TextView) v.findViewById(R.id.conver_name);
            time = (TextView) v.findViewById(R.id.conver_time);

            imageView = (CircularImageView) v.findViewById(R.id.conver_sp_image);
            linearLayout=(RelativeLayout) v.findViewById(R.id.conver_layout);

        }
    }
}
