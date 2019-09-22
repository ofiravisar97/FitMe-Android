package me.ofir.fitme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Entites.Exercise;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.R;

public class editExcerciseDialog extends AppCompatDialogFragment {

    // Vars
    TextView tvEditExc;
    TextView tvDeleteExc;
    Exercise model; // Model used as Exercise
    Workout wModel; // Model used to attach the Exercise



    public editExcerciseDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_edit_excercise, null);

        findViews(v);
        builder.setTitle("");
        builder.setView(v);

        //init Models
        getParcelables();

        // Delete btn onClick
        tvDeleteExc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteExcercise(model);
            }
        });

        tvEditExc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditDialog();
            }
        });


        return builder.create();
    }

    private void openEditDialog() {
            setEditExcerciseDialog editDialog = new setEditExcerciseDialog();
            Bundle b = new Bundle();
            b.putParcelable("model", model);
        if(wModel != null) {
            b.putParcelable("wModel", wModel);
        }
            editDialog.setArguments(b);
            editDialog.onCreateAnimation(android.R.anim.fade_in, true, android.R.anim.fade_out);
            editDialog.show(getFragmentManager(), "editExc");
            dismiss();
    }

    /**
     * Getting Parcelables models
     */
    private void getParcelables() {
        if(getArguments().getParcelable("model") != null){
            model = getArguments().getParcelable("model");
        }

        if(getArguments().getParcelable("wModel") != null){
            wModel = getArguments().getParcelable("wModel");
        }
    }

    /**
     * Deleting Excercise from DB
     * @param exercise the Excercise to delete
     */

    private void DeleteExcercise(Exercise exercise) {
            if(wModel != null){// if TRYING TO DELETE FROM WORKOUT—
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("workoutsExc").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(wModel.getWorkoutID());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot finalData: dataSnapshot.getChildren()){
                            Exercise e = finalData.getValue(Exercise.class);
                            if(e.getExerciseName().matches(exercise.getExerciseName())){
                                finalData.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dismiss();
            }
            // IF TRYING TO DELETE FROM PRE MADE WORKOUT
            else {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot finalData: dataSnapshot.getChildren()){
                            Exercise e = finalData.getValue(Exercise.class);
                            if(e.getExerciseName().matches(exercise.getExerciseName())){
                                finalData.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dismiss();
            }
    }

    private void findViews(View v) {
        tvEditExc = v.findViewById(R.id.tvEditExc);
        tvDeleteExc = v.findViewById(R.id.tvDeleteExc);
    }
}
