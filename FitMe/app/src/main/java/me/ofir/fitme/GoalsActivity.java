package me.ofir.fitme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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

import me.ofir.fitme.Dialogs.addGoalDialogFragment;
import me.ofir.fitme.Entites.Goal;
import me.ofir.fitme.ViewHolders.GoalViewHolder;
import me.ofir.fitme.ViewHolders.MealViewHolder;

public class GoalsActivity extends AppCompatActivity {
    RecyclerView rv;
    FirebaseRecyclerAdapter adapter;
    ImageButton ibAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.goals);
        setSupportActionBar(toolbar);
        rv = findViewById(R.id.rvGoals);
        ibAdd = findViewById(R.id.ibGoalAdd);
        setRv(rv);
        ibAdd.setOnClickListener((v->{
            DialogFragment fragment = new addGoalDialogFragment();
            fragment.show(getSupportFragmentManager(),null);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_workout_add) {
            DialogFragment fragment = new addGoalDialogFragment();
            fragment.show(getSupportFragmentManager(),null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRv(RecyclerView rv){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("goals").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Goal>().setQuery(ref,Goal.class).build();
        adapter = new FirebaseRecyclerAdapter<Goal,GoalViewHolder>(options) {
            @NonNull
            @Override
            public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item,viewGroup,false);
                Animation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new GoalViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull GoalViewHolder holder, int position, @NonNull Goal model) {
                holder.tvTitle.setText(" - " + model.getGoalTitle());
                holder.tvNumber.setText(String.valueOf(position+1));

                holder.goalDeleteBtn.setOnClickListener((c->{
                    deleteGoal(model);
                }));
            }
        };

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rv.setAdapter(adapter);
    }

    private void deleteGoal(Goal model) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("goals").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot finalVal: dataSnapshot.getChildren()){
                    if(finalVal.getKey().matches(model.getGoalID())){
                        finalVal.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
