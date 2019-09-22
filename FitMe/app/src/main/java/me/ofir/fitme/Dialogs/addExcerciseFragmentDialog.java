package me.ofir.fitme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import me.ofir.fitme.Entites.Exercise;
import me.ofir.fitme.Entites.Workout;
import me.ofir.fitme.R;

public class addExcerciseFragmentDialog extends AppCompatDialogFragment {

    EditText etExcName;
    EditText etExcSets;
    EditText etExcReps;
    EditText etExcWeight;
    TextView excAddBtn;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("excercises").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();

    Workout model;


    public addExcerciseFragmentDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_add_excercise, null);
        findViews(v);
        builder.setTitle(getString(R.string.addexc));
        builder.setView(v);

        if (getArguments() != null) {
            model = getArguments().getParcelable("model");
        }


        excAddBtn.setOnClickListener((c) -> {
            if (Validation()) {
                if (model != null) { // if Adding Excercise to a made workout
                    DatabaseReference wRef = FirebaseDatabase.getInstance().getReference().child("workoutsExc")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(model.getWorkoutID()).push();
                    Exercise e  = new Exercise(etExcName.getText().toString(),
                            Integer.parseInt(etExcSets.getText().toString()),
                            Integer.parseInt(etExcReps.getText().toString()),
                            Float.parseFloat(etExcWeight.getText().toString()));
                    e.setWorkoutID(model.getWorkoutID());

                    addExc(e,wRef);
                } else { // if Adding Exc to a New Workout
                    addExc(new Exercise(etExcName.getText().toString(),
                                    Integer.parseInt(etExcSets.getText().toString()),
                                    Integer.parseInt(etExcReps.getText().toString()),
                                    Float.parseFloat(etExcWeight.getText().toString())),
                            ref);
                }
            }
        });
        return builder.create();
    }

    private void addExc(Exercise exercise, DatabaseReference ref) {
        ref.setValue(exercise);
        dismiss();
    }

    private void findViews(View v) {
        etExcName = v.findViewById(R.id.etExcName);
        etExcReps = v.findViewById(R.id.etExcReps);
        etExcSets = v.findViewById(R.id.etExcSets);
        excAddBtn = v.findViewById(R.id.tvExcerciseAddBtn);
        etExcWeight = v.findViewById(R.id.etExcWeight);
    }

    private boolean Validation() {
        if (etExcName.getText().length() == 0) etExcName.setError(getString(R.string.namerequired));
        if (etExcSets.getText().length() == 0) etExcSets.setError("Sets required.");
        if (etExcReps.getText().length() == 0) etExcReps.setError("Reps required.");
        if (etExcWeight.getText().length() == 0) etExcWeight.setError("Weight required");
        //TODO: ADD LAST WEIGHT CONSTRUCTOR AND EDIT TEXT SO THE ADAPTER CAN SET LAST WEIGHT
        //TODO: EXCERCISES EDITABLE

        if (etExcName.getText().length() == 0 || etExcReps.getText().length() == 0 || etExcSets.getText().length() == 0 || etExcWeight.getText().length() == 0) {
            return false;
        }

        return true;
    }


}
