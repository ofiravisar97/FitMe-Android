package me.ofir.fitme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.ofir.fitme.Dialogs.addExcerciseFragmentDialog;
import me.ofir.fitme.Dialogs.editExcerciseDialog;
import me.ofir.fitme.Entites.Exercise;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.ViewHolders.ExcerciseViewHolder;

public class workoutDetailsActivity extends AppCompatActivity {
    TextView tvWorkoutDetailsName;
    TextView tvWorkoutDetailsDesc;
    RecyclerView rv;
    FirebaseRecyclerOptions<Exercise> options;
    FirebaseRecyclerAdapter<Exercise, ExcerciseViewHolder> adapter;
    ImageButton btnAddExc;

    Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnAddExc = findViewById(R.id.btnAddExcDetails);

        //Getting Model
         workout = getIntent().getParcelableExtra("model");
        // Setting title as Workout name
        toolbar.setTitle(workout.getWorkoutName());
        // Setting Toolbar
        setSupportActionBar(toolbar);

        //Getting ref
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("workoutsExc").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(workout.getWorkoutID());
        //Setting Adapter
        setAdapter(ref);
        //Setting Recycler
        settingRecycler();

        //Add exc Click Listner
        btnAddExc.setOnClickListener((c->{
            openAddExcDialog(workout);
        }));

        tvWorkoutDetailsName = findViewById(R.id.tvWorkoutDetailName);
        tvWorkoutDetailsName.setText(workout.getWorkoutName());

        tvWorkoutDetailsDesc = findViewById(R.id.tvWorkoutDetailsDesc);
        tvWorkoutDetailsDesc.setText(workout.getWorkoutDescription());

    }

    private void openAddExcDialog(Workout workout) {
        addExcerciseFragmentDialog excerciseFragmentDialog = new addExcerciseFragmentDialog();
        Bundle arguments = new Bundle();
        arguments.putParcelable("model",workout);
        excerciseFragmentDialog.setArguments(arguments);
        excerciseFragmentDialog.show(getSupportFragmentManager(), "addExcercise"); // Showing Excercise Dialog
    }

    //Setting Adapter
    private void setAdapter(DatabaseReference reference) {
        options = new FirebaseRecyclerOptions.Builder<Exercise>().setQuery(reference, Exercise.class).build();
        adapter = new FirebaseRecyclerAdapter<Exercise, ExcerciseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ExcerciseViewHolder holder, int position, @NonNull Exercise model) {

                holder.etExcerciseName.setText(model.getExerciseName());
                holder.etExcerciseSets.setText(getString(R.string.sets)+ String.valueOf(model.getSets()));
                holder.etExcerciseReps.setText(getString(R.string.reps)+ String.valueOf(model.getReps()));
                holder.tvExcerciseWeight.setText(getString(R.string.weight_cm) + String.valueOf(model.getLastWeight()));

                holder.itemView.setOnClickListener((c->{
                    editExcerciseDialog editExerciseDialog = new editExcerciseDialog();
                    Bundle b = new Bundle();
                    b.putParcelable("model", model);
                    b.putParcelable("wModel", workout);
                    editExerciseDialog.setArguments(b);
                    editExerciseDialog.show(getSupportFragmentManager(),"edit");
                }));
            }

            @NonNull
            @Override
            public ExcerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //Inflating View
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.excercise_item,viewGroup,false);
                //Setting Animation
                Animation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new ExcerciseViewHolder(v);
            }
        };
    }

    //Setting Recycler
    private void settingRecycler() {
        rv = findViewById(R.id.rvWorkoutDetailsExc);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rv.setAdapter(adapter);
    }


    // Listening to Adapter
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
