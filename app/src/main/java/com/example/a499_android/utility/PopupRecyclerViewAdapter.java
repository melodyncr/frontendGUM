package com.example.a499_android.utility;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a499_android.LandingPage;
import com.example.a499_android.MessageAdapter;
import com.example.a499_android.R;
import com.example.a499_android.SelectWorkout;

import java.util.ArrayList;

public class PopupRecyclerViewAdapter extends RecyclerView.Adapter<PopupRecyclerViewAdapter.ItemHolder>{

    private Context mContext;
    private String data[];
    private String choice;
    private PopupWindow popUp;

    public PopupRecyclerViewAdapter(Context mContext, String data[], PopupWindow popUp) {
        this.mContext = mContext;
        this.data = data;
        this.popUp = popUp;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new  ItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.bind(data[position]);
    }

    @Override
    public int getItemCount() { return data.length; }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        public void bind(String obj) {
            final TextView item = itemView.findViewById(R.id.item_id);
            item.setText(obj);
            //make item clickable
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save selected object
                    choice = data[getAdapterPosition()];
                    if(SelectWorkout.difficulty.equals("easy")){
                        choice = choice + "_Gentle_" + LandingPage.avatarName;
                    }else if(SelectWorkout.difficulty.equals("medium")){
                        choice = choice + "_Moderate_" + LandingPage.avatarName;
                    }else {
                        choice = choice + "_Vigorous_" + LandingPage.avatarName;
                    }
                    SelectWorkout.selectedWorkout = choice;
                    popUp.dismiss();
                    Toast.makeText(mContext, choice, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}