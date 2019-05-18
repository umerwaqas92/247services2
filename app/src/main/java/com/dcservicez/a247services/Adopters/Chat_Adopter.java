package com.dcservicez.a247services.Adopters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcservicez.a247services.R;
import com.dcservicez.a247services.objects.Chat_Itm;

import java.util.ArrayList;

public class Chat_Adopter extends RecyclerView.Adapter<Chat_Adopter.ViewHolder>{

    ArrayList<Chat_Itm> chat_itms;
    private Context context;

    public Chat_Adopter(ArrayList<Chat_Itm> chat_itms, Context context) {
        this.chat_itms = chat_itms;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(chat_itms.get(viewType).getMsg_type()==1){
            ///sent
            view= LayoutInflater.from(context).inflate(R.layout.chat_sent_itm,parent,false);
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.chat_item_recieve,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Chat_Itm chatItm=chat_itms.get(position);
            holder.textView.setText(chatItm.getMsg());
    }

    @Override
    public int getItemCount() {
        return chat_itms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.chat_txt_view);
        }
    }
}
