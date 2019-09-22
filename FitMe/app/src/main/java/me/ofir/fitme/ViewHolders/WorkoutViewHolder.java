package me.ofir.fitme.ViewHolders;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import me.ofir.fitme.R;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    public TextView tvWorkoutName;
    public TextView tvWorkoutDesc;
    public ImageButton workoutDeletebtn;
    public ConstraintLayout workoutContainer;

    public WorkoutViewHolder(@NonNull View itemView) {
        super(itemView);
        tvWorkoutName = itemView.findViewById(R.id.workoutItemName);
        tvWorkoutDesc = itemView.findViewById(R.id.workoutItemDesc);
        workoutDeletebtn = itemView.findViewById(R.id.workoutItemDeleteBtn);
        workoutContainer = itemView.findViewById(R.id.workoutContainer);
    }
}
