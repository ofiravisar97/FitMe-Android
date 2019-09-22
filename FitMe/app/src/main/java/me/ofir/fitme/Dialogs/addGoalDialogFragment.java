package me.ofir.fitme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.ofir.fitme.Entites.Goal;
import me.ofir.fitme.R;

public class addGoalDialogFragment extends AppCompatDialogFragment {
    EditText etGoalName;
    EditText etGoalDescription;

    public addGoalDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_goal, null);
        findViews(v);
        builder.setTitle(getString(R.string.add_goal));
        builder.setView(v);

        builder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addGoal();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void findViews(View v) {
        etGoalName = v.findViewById(R.id.etGoalTitle);
        etGoalDescription = v.findViewById(R.id.etGoalDesc);
    }

    private void addGoal() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("goals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
        String id = ref.getKey();
        ref.setValue(new Goal(etGoalName.getText().toString(),
                etGoalDescription.getText().toString(),
                id)).addOnSuccessListener((s -> {
            //Toast.makeText(getActivity(), getString(R.string.goal_add_successfully), Toast.LENGTH_SHORT).show();
            dismiss();
        })).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(etGoalName, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
