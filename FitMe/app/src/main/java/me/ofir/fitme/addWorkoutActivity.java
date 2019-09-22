package me.ofir.fitme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.ofir.fitme.Dialogs.addExcerciseFragmentDialog;
import me.ofir.fitme.Dialogs.editExcerciseDialog;
import me.ofir.fitme.Entites.Exercise;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.Entites.myExcercisesCallback;
import me.ofir.fitme.ViewHolders.ExcerciseViewHolder;

public class addWorkoutActivity extends AppCompatActivity {

    RecyclerView rv;
    EditText etWorkoutName,etWorkoutDesc;
    Button addExcerciseBtn;
    Button doneBtn;
    FirebaseRecyclerOptions<Exercise> options;
    FirebaseRecyclerAdapter<Exercise, ExcerciseViewHolder> adapter;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        findViews();

        addExcerciseBtn.setOnClickListener((c)->{
            hideKeyboard(); // Hiding Keyboard
            addExcerciseFragmentDialog excerciseFragmentDialog = new addExcerciseFragmentDialog();
            excerciseFragmentDialog.show(getSupportFragmentManager(), "addExcercise"); // Showing Excercise Dialog
        });
        doneBtn.setOnClickListener((c)->{
            hideKeyboard(); // Hiding Keyboard
            addWorkout(); // Adding Workout
        });
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL);
        rv.addItemDecoration(decoration);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setAdapter();
    }

    private  void findViews(){
        rv = findViewById(R.id.rvAddWorkout);
        addExcerciseBtn = findViewById(R.id.workoutAddExcBtn);
        doneBtn = findViewById(R.id.addWorkoutDoneBtn);
        etWorkoutName = findViewById(R.id.etWorkoutName);
        etWorkoutDesc = findViewById(R.id.etWorkoutDesc);
    }

    private void setAdapter() {
        options = new FirebaseRecyclerOptions.Builder<Exercise>().setQuery(ref,Exercise.class).build();
        adapter = new FirebaseRecyclerAdapter<Exercise,ExcerciseViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull ExcerciseViewHolder holder, int position, @NonNull Exercise model) {
                   holder.etExcerciseName.setText(model.getExerciseName());
                   holder.etExcerciseSets.setText(getString(R.string.sets) + String.valueOf(model.getSets()));
                   holder.etExcerciseReps.setText(getString(R.string.reps) + String.valueOf(model.getReps()));
                   holder.tvExcerciseWeight.setText(getString(R.string.weight_cm) + String.valueOf(model.getLastWeight()));


                   holder.itemView.setOnClickListener((c)->{
                       editExcerciseDialog editExerciseDialog = new editExcerciseDialog();
                       Bundle b = new Bundle();
                       b.putParcelable("model", model);
                       editExerciseDialog.setArguments(b);
                       editExerciseDialog.show(getSupportFragmentManager(),"edit");
                   });

            }

            @NonNull
            @Override
            public ExcerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.excercise_item,viewGroup,false);
                Animation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(2000);
                v.setAnimation(anim);

                return new ExcerciseViewHolder(v);
            }
        };
        rv.setAdapter(adapter);
    }

    private boolean Validation(){
        if(etWorkoutName.getText().length() == 0) etWorkoutName.setError("Name required.");
        if(etWorkoutDesc.getText().length() == 0) etWorkoutDesc.setError("Description required.");
        if(adapter.getSnapshots().size() == 0) addExcerciseBtn.setError("Excercises required");

        return etWorkoutDesc.getText().length() != 0 && etWorkoutName.getText().length() != 0 && adapter.getSnapshots().size() != 0;
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

    // Clearing Excercise Table on Pause or Exit
    @Override
    protected void onPause() {
        super.onPause();
        clearExcercises();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter !=null)adapter.startListening();
    }


    /**
     * Method that Adding Workout if Validation Succed
     */
    private void addWorkout(){
        if(Validation()){ // Checking for Validation
            getExcercises(new myExcercisesCallback() { // Getting Excercies List
                @Override
                public void onCallback(ArrayList<Exercise> exercises) {
                    // Getting Ref
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("workouts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
                    String id = ref.getKey();// Getting Workout ID
                    DatabaseReference ExcRef = FirebaseDatabase.getInstance().getReference().child("workoutsExc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    // Setting Workout
                    Workout workout = new Workout(etWorkoutName.getText().toString(),etWorkoutDesc.getText().toString(),exercises,id);
                    for(int i = 0;i < exercises.size(); i++){
                        exercises.get(i).setWorkoutID(id);
                        ExcRef.child(workout.getWorkoutID()).push().setValue(exercises.get(i));
                    }
                    // Uploading Workout to Database
                    ref.setValue(workout).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Clearing Excercises from Database
                            clearExcercises();
                            //Sending Toast
                            Toast.makeText(addWorkoutActivity.this, "Workout Added.", Toast.LENGTH_SHORT).show();
                            //Moving back to Workout Activity
                            doneBtn.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(),WorkoutsActivity.class));
                                }
                            },1000);
                        }
                    });
                }
            });
        }
    }

    private void getExcercises(myExcercisesCallback callback) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        Exercise e = child.getValue(Exercise.class);
                        exercises.add(e);
                    }
                    callback.onCallback(exercises);
                    System.out.println(exercises);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Clear Temp Excercises Table
    private void clearExcercises(){
        FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
    }

    //Hiding Keyboard
    private void hideKeyboard(){
        View v = this.getCurrentFocus();
        if(v != null){
            InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}
