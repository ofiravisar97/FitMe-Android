package me.ofir.fitme.ViewHolders;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import me.ofir.fitme.R;

public class MealViewHolder extends RecyclerView.ViewHolder {
    public ConstraintLayout mealContainer;
    public TextView Category, Hour, Content;
    public ImageButton mealDeleteBtn;
    public ImageButton mealAlarmBtn;

    public MealViewHolder(@NonNull View itemView) {
        super(itemView);
        mealDeleteBtn = itemView.findViewById(R.id.workoutItemDeleteBtn);
        Category = itemView.findViewById(R.id.workoutItemName);
        Hour = itemView.findViewById(R.id.mealHour);
        Content = itemView.findViewById(R.id.mealContent);
        mealContainer = itemView.findViewById(R.id.mealContainer);
    }
}
