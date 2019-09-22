package me.ofir.fitme.ViewHolders;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import me.ofir.fitme.R;

public class GoalViewHolder extends RecyclerView.ViewHolder {
   public TextView tvTitle;
   public TextView tvNumber;
   public ImageButton goalDeleteBtn;

    public GoalViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvGoalTitle);
        tvNumber = itemView.findViewById(R.id.tvGoalNum);
        goalDeleteBtn = itemView.findViewById(R.id.goalDeleteBtn);
    }
}
