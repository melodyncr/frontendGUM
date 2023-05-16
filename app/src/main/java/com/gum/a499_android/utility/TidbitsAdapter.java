package com.gum.a499_android.utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gum.a499_android.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TidbitsAdapter extends RecyclerView.Adapter<TidbitsAdapter.MyViewHolder> {

    List<String> listOfTidbits;
    List<String> listOfTidbitsIds;
    Context context;
    CollectionReference tidbits;
    ImageButton deleteTidbitBtn;

    public TidbitsAdapter(Context ct, List<String> tidbitsIds,List<String> tidbitsList) {
        context = ct;
        listOfTidbits = tidbitsList;
        listOfTidbitsIds = tidbitsIds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tidbits_row, parent, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tidbits = db.collection("Tidbits");
        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tidbitId.setText(listOfTidbitsIds.get(position));
        holder.tidbit.setText(listOfTidbits.get(position));
    }

    @Override
    public int getItemCount() {
        int size = listOfTidbits.size(); // for some reason, returning listOfTidbits.size() directly doesn't work
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tidbit;
        TextView tidbitId;
        public MyViewHolder(@NonNull View itemView, TidbitsAdapter adapter) {
            super(itemView);
            tidbit = itemView.findViewById(R.id.tidbitText);
            tidbitId = itemView.findViewById(R.id.tidbitId);
            deleteTidbitBtn = itemView.findViewById(R.id.deleteTidbitBtn);

            deleteTidbitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tidbitIdToBeDeleted = listOfTidbitsIds.get(getAdapterPosition());

                    tidbits.document(tidbitIdToBeDeleted).delete().addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Success", "Tidbit has been deleted");
                                    Toast.makeText(context,
                                            "Tidbit Deleted",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failure", e.getMessage());
                        }
                    });
                    listOfTidbitsIds.remove(getAdapterPosition());
                    listOfTidbits.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
