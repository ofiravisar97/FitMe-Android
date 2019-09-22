package me.ofir.fitme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import me.ofir.fitme.Entites.Meal;

public class MealDetailsActivity extends AppCompatActivity {

    TextView tvHour,tvContent,tvName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.meals));
        setSupportActionBar(toolbar);
        // Finding Views
        findViews();
        //Setting UI
        setUI(getMeal());
    }

    private void findViews() {
        tvHour = findViewById(R.id.tvMealDetailsHour);
        tvContent = findViewById(R.id.tvMealDetailsContent);
        tvName = findViewById(R.id.tvMealDetailsName);
    }

    private Meal getMeal() {
        Intent intent = getIntent();
        return intent.getParcelableExtra("model");
    }

    private void setUI(Meal m){
            tvHour.setText(m.getHour());
            tvContent.setText(m.getContent());
            tvName.setText(m.getName());
    }
}
