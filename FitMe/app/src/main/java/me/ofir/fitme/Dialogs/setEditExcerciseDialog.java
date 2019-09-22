package me.ofir.fitme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

public class setEditExcerciseDialog extends AppCompatDialogFragment {

    TextView btnCancel,btnDone;
    EditText etLastWeight,etSets,etReps;
    Exercise model;
    Workout wModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_setedit_excercise, null);
        findViews(v);
        //getting Models
        getModels();

        builder.setMessage(model.getExerciseName());
        builder.setTitle(getString(R.string.set_exc));
        builder.setView(v);
        //Setting UI
        setText();

        btnCancel.setOnClickListener((c)->{
            dismiss();
        });

        btnDone.setOnClickListener((c)->{
            setEdit(new Exercise(model.getExerciseName(),
                    Integer.parseInt(etSets.getText().toString()),
                    Integer.parseInt(etReps.getText().toString()),
                    Float.parseFloat(etLastWeight.getText().toString())));
        });
        return builder.create();
    }

    private void setEdit(Exercise exercise) {
        if(wModel != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("workoutsExc").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(wModel.getWorkoutID());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot finalVal : dataSnapshot.getChildren()){
                        Exercise e = finalVal.getValue(Exercise.class);
                        if(exercise.getExerciseName().matches(e.getExerciseName())){
                            finalVal.getRef().setValue(exercise);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dismiss();
        }

        else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot finalVal : dataSnapshot.getChildren()){
                        Exercise e = finalVal.getValue(Exercise.class);
                        if(exercise.getExerciseName().matches(e.getExerciseName())){
                            finalVal.getRef().setValue(exercise);
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
        btnCancel = v.findViewById(R.id.cancelBtnEdit);
        btnDone = v.findViewById(R.id.doneBtnEdit);
        etLastWeight = v.findViewById(R.id.etLastWeightEdit);
        etReps = v.findViewById(R.id.etRepsEdit);
        etSets = v.findViewById(R.id.etSetsEdit);
    }

    private void setText(){
        etLastWeight.setText(String.valueOf(model.getLastWeight()));
        etSets.setText(String.valueOf(model.getSets()));
        etReps.setText(String.valueOf(model.getReps()));
    }

    private void getModels(){
        if(getArguments().getParcelable("model") != null){
            model = getArguments().getParcelable("model");
        }

        if(getArguments().getParcelable("wModel") != null){
            wModel = getArguments().getParcelable("wModel");
        }
    }
}
