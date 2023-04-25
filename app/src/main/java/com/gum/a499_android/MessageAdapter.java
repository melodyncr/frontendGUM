package com.gum.a499_android;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ItemHolder>{

    public String TAG = "Message Adapter";
    ArrayList<String> messagesList;
    public MessageAdapter(){

    }
    public MessageAdapter(ArrayList<String> messagesList){
        this.messagesList=messagesList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new  ItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.bind(messagesList.get(position));
    }

    @Override
    public int getItemCount(){return messagesList.size();}

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.message_item, parent, false));
        }
        public void bind(String f ) {
            TextView item = itemView.findViewById(R.id.message_id);
            item.setText(f);

        }
    }

}
