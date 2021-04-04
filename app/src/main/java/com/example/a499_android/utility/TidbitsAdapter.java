package com.example.a499_android.utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a499_android.R;

import org.w3c.dom.Text;

import java.util.List;

public class TidbitsAdapter extends RecyclerView.Adapter<TidbitsAdapter.MyViewHolder> {

    List<String> listOfTidbits;
    Context context;

    public TidbitsAdapter(Context ct, List<String> tidbitsList) {
        context = ct;
        listOfTidbits = tidbitsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tidbits_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tidbit.setText(listOfTidbits.get(position));
    }

    @Override
    public int getItemCount() {
        int size = listOfTidbits.size(); // for some reason, returning listOfTidbits.size() directly doesn't work
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tidbit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tidbit = itemView.findViewById(R.id.tidbitsText);
        }
    }
}
