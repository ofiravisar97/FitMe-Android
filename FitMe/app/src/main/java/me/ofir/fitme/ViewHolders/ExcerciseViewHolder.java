package me.ofir.fitme.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.ofir.fitme.R;

public class ExcerciseViewHolder extends RecyclerView.ViewHolder {

    public TextView etExcerciseName,etExcerciseSets,etExcerciseReps,tvExcerciseWeight;


    public ExcerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        etExcerciseName = itemView.findViewById(R.id.tvExcerciseItemName);
        etExcerciseReps = itemView.findViewById(R.id.tvExcerciseItemReps);
        etExcerciseSets = itemView.findViewById(R.id.tvExcerciseItemSets);
        tvExcerciseWeight = itemView.findViewById(R.id.tvExcerciseItemWeight);
    }
}
