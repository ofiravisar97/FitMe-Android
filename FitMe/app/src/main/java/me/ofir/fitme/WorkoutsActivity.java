package me.ofir.fitme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Entites.Exercise;
import me.ofir.fitme.Entites.Meal;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.ViewHolders.MealViewHolder;
import me.ofir.fitme.ViewHolders.WorkoutViewHolder;

public class WorkoutsActivity extends AppCompatActivity {

    //Vars
    RecyclerView rv;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("workouts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    FirebaseRecyclerOptions<Workout> options;
    FirebaseRecyclerAdapter<Workout, WorkoutViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getString(R.string.workouts));
        setSupportActionBar(toolbar);

        //Setting Recycler and Firebase Adapter
        setAdapter();
        settingRecycler();
        findViewById(R.id.ibWorkoutAdd).setOnClickListener((v->{
            startActivity(new Intent(getApplicationContext(), addWorkoutActivity.class)); // Opening Add Workout Activity
        }));
    }

    //Used for Scrolling menu collapsing listner
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setScrolling(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Setting Firebase Adapter
     */
    private void setAdapter() {
        options = new FirebaseRecyclerOptions.Builder<Workout>().setQuery(ref, Workout.class).build();
        adapter = new FirebaseRecyclerAdapter<Workout, WorkoutViewHolder>(options) {
            @NonNull
            @Override
            public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //Inflating View
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_item, viewGroup, false);
                //Setting Animation
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new WorkoutViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, @NonNull Workout model) {
                //Setting item UI
                holder.tvWorkoutDesc.setText(model.getWorkoutDescription());
                holder.tvWorkoutName.setText(model.getWorkoutName());

                holder.workoutDeletebtn.setOnClickListener((c) -> {
                    deleteWorkout(model);
                });

                holder.workoutContainer.setOnClickListener(view -> moveToWorkoutDetails(model));
            }
        };
    }

    //Sending To Workout Details Activity with Parcablle
    private void moveToWorkoutDetails(Workout model) {
        Intent i = new Intent(this, workoutDetailsActivity.class);
        i.putExtra("model", model);
        startActivity(i);
    }

    //Setting Recycler
    private void settingRecycler() {
        rv = findViewById(R.id.rvWorkouts);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rv.setAdapter(adapter);
    }


    // Listening to Adapter
    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    //Stop Listening to Adapter when Activity Stops
    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }

    //Listening Again
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.startListening();
    }

    /**
     * Deleting Workout from Database
     *
     * @param workout the Workout model of an itemview to Delete
     */
    private void deleteWorkout(Workout workout) {
        FirebaseDatabase.getInstance().getReference().child("workouts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Workout w = child.getValue(Workout.class);
                    if (w.getWorkoutID().matches(workout.getWorkoutID())) {
                        removeExc(child.getKey(),workout);
                        child.getRef().removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workouts, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_workout_add) {
            startActivity(new Intent(getApplicationContext(), addWorkoutActivity.class)); // Opening Add Workout Activity
        }

        return super.onOptionsItemSelected(item);
    }

    // Removing Attached Excercises from a workout *** Used for Delete Workout Method
    private void removeExc(String id,Workout workout) {
        FirebaseDatabase.getInstance().getReference().child("workoutsExc").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(workout.getWorkoutID()).removeValue();
    }

    private void setScrolling(Menu menu) {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_work);
        AppBarLayout appBar = findViewById(R.id.app_bar_meals);
        MenuItem item = menu.getItem(0);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShowing = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBar.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    isShowing = true;
                    item.setVisible(true);
                } else if (isShowing) {
                    isShowing = false;
                    item.setVisible(false);
                }
            }
        });
    }
}
