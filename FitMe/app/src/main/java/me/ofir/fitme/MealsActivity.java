package me.ofir.fitme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.ViewHolders.MealViewHolder;
import me.ofir.fitme.Entites.Meal;

public class MealsActivity extends AppCompatActivity {
    RecyclerView rv;
    DatabaseReference ref;
    FirebaseRecyclerOptions<Meal> options;
    FirebaseRecyclerAdapter<Meal, MealViewHolder> adapter;
    int lastPos = -1;
    ImageButton ibAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.meals));
        setSupportActionBar(toolbar);
        rv = findViewById(R.id.rvMeals);
        ibAdd = findViewById(R.id.ibMealAdd);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ref = FirebaseDatabase.getInstance().getReference().child("meals").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        setAdapter();

        ibAdd.setOnClickListener((v->{
            openDialog();
        }));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workouts,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setScrolling(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_workout_add) {
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAdapter() {
        options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref,Meal.class).build();
        adapter = new FirebaseRecyclerAdapter<Meal,MealViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MealViewHolder holder, int position, @NonNull Meal model) {
                holder.Category.setText(model.getName());
                holder.Content.setText(model.getContent());
                holder.Hour.setText(model.getHour());

                startAnimate(holder.itemView,position);

                holder.mealDeleteBtn.setOnClickListener((c)->{
                    deleteMeal(new Meal(holder.Category.getText()+"",holder.Content.getText()+"",holder.Hour.getText()+""));
                });

               holder.mealContainer.setOnClickListener((c->{
                   movingToMealDetails(model);
               }));

                //TODO:MAKE ALARM BTN AND PUSH NOTIFICATION
            }

            @NonNull
            @Override
            public MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meal_item,viewGroup,false);
                Animation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

               return new MealViewHolder(v);
            }
        };
        rv.setAdapter(adapter);
    }

    private void movingToMealDetails(@NonNull Meal model) {
        Intent i = new Intent(getApplicationContext(), MealDetailsActivity.class);
        i.putExtra("model",model);
        startActivity(i);
    }

    private void startAnimate(View v,int pos) {
        if(pos > lastPos) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            animation.setDuration(1500);
            v.setAnimation(animation);
            lastPos = pos;
        }
    }

    private void openDialog() {
        addMealFragment mealFragment = new addMealFragment();
        mealFragment.show(getSupportFragmentManager(), "addMeal");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter !=null)adapter.startListening();
    }

    private void deleteMeal(Meal meal){
        FirebaseDatabase.getInstance().getReference().child("meals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Meal m = child.getValue(Meal.class);
                    if(meal.getName().matches(m.getName()) && m.getContent().matches(meal.getContent())){
                        child.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Scroll listner
    private void setScrolling(Menu menu){
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_meals);
        AppBarLayout appBar = findViewById(R.id.app_bar_meals);
        MenuItem item = menu.getItem(0);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShowing = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange == -1){
                    scrollRange = appBar.getTotalScrollRange();
                }
                if(scrollRange + i == 0){
                    isShowing = true;
                    item.setVisible(true);
                } else if(isShowing){
                    isShowing = false;
                    item.setVisible(false);
                }
            }
        });
    }
}
