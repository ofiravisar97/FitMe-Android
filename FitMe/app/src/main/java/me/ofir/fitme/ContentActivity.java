package me.ofir.fitme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Asyncs.NewsAsyncTask;
import me.ofir.fitme.Dialogs.RateUsFragment;
import me.ofir.fitme.Entites.Goal;
import me.ofir.fitme.Entites.Meal;
import me.ofir.fitme.Entites.User;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.Dialogs.RateUsFragment;
import me.ofir.fitme.ViewHolders.GoalViewHolder;
import me.ofir.fitme.ViewHolders.MealViewHolder;
import me.ofir.fitme.ViewHolders.WorkoutViewHolder;

public class ContentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RESULT_CODE = 100;
    TextView tvNavName;
    TextView tvWelcomeMsg;
    TextView tvMealsSeeAll;
    TextView tvWorkoutsSeeAll;
    TextView tvGoalsSeeAll;
    String userDisplay;

    //Recyclers
    RecyclerView rvMeals;
    RecyclerView rvWorkouts;
    RecyclerView rvNews;
    RecyclerView rvGoals;

    //Firebase Adapters
    FirebaseRecyclerOptions<Meal> mealsOptions;
    FirebaseRecyclerAdapter<Meal, MealViewHolder> mealsAdapter;

    FirebaseRecyclerOptions<Workout> workoutsOptions;
    FirebaseRecyclerAdapter<Workout, WorkoutViewHolder> workoutsAdapter;

    FirebaseRecyclerOptions<Goal> goalOptions;
    FirebaseRecyclerAdapter<Goal, GoalViewHolder> goalAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
           Intent i = new Intent(this, MainActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        findViews();
        userDisplay = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        View Header = navigationView.getHeaderView(0);
        TextView displayName = Header.findViewById(R.id.tvNavName);
        displayName.setText(userDisplay);
        Toast.makeText(this, getString(R.string.hi) + userDisplay, Toast.LENGTH_SHORT).show();
        checkProfile();

        setUI();
        setRvMeals(FirebaseDatabase.getInstance().getReference().child("meals").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        );
        setRvWorkouts(FirebaseDatabase.getInstance().getReference().child("workouts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        bringNews(rvNews);
        setRvGoals(rvGoals);

    }

    private void findViews() {
        tvNavName = findViewById(R.id.tvNavName);
        rvMeals = findViewById(R.id.rvMainMeals);
        tvWelcomeMsg = findViewById(R.id.tvWelcomeMsg);
        tvMealsSeeAll = findViewById(R.id.tvMealsSeeAll);
        rvWorkouts = findViewById(R.id.rvMainWorkouts);
        tvWorkoutsSeeAll = findViewById(R.id.tvWorkoutsSeeAll);
        rvNews = findViewById(R.id.rvNews);
        rvGoals = findViewById(R.id.rvMainGoals);
        tvGoalsSeeAll = findViewById(R.id.tvGoalsSeeAll);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_meals) {
            startActivity(new Intent(this, MealsActivity.class));
        } else if (id == R.id.nav_workouts) {
            startActivity(new Intent(this, WorkoutsActivity.class));
        } else if (id == R.id.nav_about) {
            findViewById(R.id.Frame).setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.Frame, new AboutFragment()).commit();
        } else if(id == R.id.nav_rate){
            RateUsFragment rateUsFragment = new RateUsFragment();
            rateUsFragment.show(getSupportFragmentManager(),"rate");
        } else if(id == R.id.nav_share){
            inviteClicked();
        } else if(id == R.id.nav_goals){
            startActivity(new Intent(getApplicationContext(), GoalsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Checking if user have profile Setted
     */
    private void checkProfile() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if (!u.isHaveProfile()) {
                    Intent i = new Intent(getApplicationContext(), StartProfileActivity.class);
                    // Cleaning Back Stack so user Have to go Through Profile Set up
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //Finishing Main Activity
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void setRvMeals(DatabaseReference reference) {
        mealsOptions = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(reference, Meal.class).build();
        mealsAdapter = new FirebaseRecyclerAdapter<Meal, MealViewHolder>(mealsOptions) {

            @Override
            protected void onBindViewHolder(@NonNull MealViewHolder holder, int position, @NonNull Meal model) {
                holder.Category.setText(model.getName());
                holder.Content.setText(model.getContent());
                holder.Hour.setText(model.getHour());

//                startAnimate(holder.itemView,position);

                holder.mealDeleteBtn.setVisibility(View.GONE);

//                holder.mealContainer.setOnClickListener((c->{
//                    movingToMealDetails(model);
//                }));
            }

            @NonNull
            @Override
            public MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meal_item, viewGroup, false);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new MealViewHolder(v);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                 if (getItemCount() == 0) {
                    //tv.setText
                    tvMealsSeeAll.setText(getString(R.string.youdonthavemeals));
                }else {
                     tvMealsSeeAll.setText(getString(R.string.see_all));
                 }
            }
        };


        rvMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMeals.setAdapter(mealsAdapter);
    }


    private void setRvWorkouts(DatabaseReference reference){
        workoutsOptions = new FirebaseRecyclerOptions.Builder<Workout>().setQuery(reference, Workout.class).build();
        workoutsAdapter = new FirebaseRecyclerAdapter<Workout, WorkoutViewHolder>(workoutsOptions) {

            @Override
            protected void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, @NonNull Workout model) {
                holder.tvWorkoutName.setText(model.getWorkoutName());
                holder.tvWorkoutDesc.setText(model.getWorkoutDescription());

//

                holder.workoutDeletebtn.setVisibility(View.GONE);

//
            }

            @NonNull
            @Override
            public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_item,viewGroup,false);
                Animation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new WorkoutViewHolder(v);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(workoutsAdapter.getSnapshots().size() == 0){
                    tvWorkoutsSeeAll.setText(getString(R.string.youdonthavework));
                } else tvWorkoutsSeeAll.setText(getString(R.string.see_all));
            }
        };


        rvWorkouts.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        rvWorkouts.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rvWorkouts.setAdapter(workoutsAdapter);
    }

    private void setRvGoals(RecyclerView rv){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("goals").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        goalOptions = new FirebaseRecyclerOptions.Builder<Goal>().setQuery(ref,Goal.class).build();
        goalAdapter = new FirebaseRecyclerAdapter<Goal, GoalViewHolder>(goalOptions) {
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

                holder.goalDeleteBtn.setVisibility(View.GONE);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(goalAdapter.getItemCount() <= 0){
                    tvGoalsSeeAll.setText(getString(R.string.you_dont_goals));
                } else {
                    tvGoalsSeeAll.setText(getString(R.string.see_all));
                }
            }
        };

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rv.setAdapter(goalAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mealsAdapter != null) {
            mealsAdapter.startListening();
        }
        if(workoutsAdapter != null) {
            workoutsAdapter.startListening();
        }
        if(goalAdapter != null){
            goalAdapter.startListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mealsAdapter != null) {
            mealsAdapter.startListening();
        }
        if(workoutsAdapter != null) {
            workoutsAdapter.startListening();
        }
        if(goalAdapter != null){
            goalAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mealsAdapter != null) {
            mealsAdapter.stopListening();
        }
        if(workoutsAdapter != null) {
            workoutsAdapter.stopListening();
        }
        if(goalAdapter != null){
            goalAdapter.stopListening();
        }
    }

    private void setUI() {
        tvWelcomeMsg.setText(getString(R.string.welcome) + " " + userDisplay);

        tvMealsSeeAll.setOnClickListener((view) -> {
            startActivity(new Intent(this, MealsActivity.class));
        });
        tvWorkoutsSeeAll.setOnClickListener((view -> {
            startActivity(new Intent(this,WorkoutsActivity.class));
        }));
        tvGoalsSeeAll.setOnClickListener((view ->{
            startActivity(new Intent(this,GoalsActivity.class));
        }));
    }

    private  void bringNews(RecyclerView rvNews){
        new NewsAsyncTask(rvNews,this).execute();
    }

    private void inviteClicked(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invite_ur))
                .setMessage("Hello, you wanna checkout that amazing app.")
                .setDeepLink(Uri.parse("https://www.google.com"))
                .setCallToActionText("Invation CTA")
                .build();
        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE){
            if(resultCode == RESULT_OK){
                String [] ids = AppInviteInvitation.getInvitationIds(resultCode,data);
            }
        }
    }
}
