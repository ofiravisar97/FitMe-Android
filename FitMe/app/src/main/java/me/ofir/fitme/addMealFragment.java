package me.ofir.fitme;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import me.ofir.fitme.Entites.Meal;


/**
 * A simple {@link Fragment} subclass.
 */
public class addMealFragment extends AppCompatDialogFragment {

    /**
     * --------------- FRAGMENT USED TO ADD A MEAL -------------------
     */

    //Some Vars
    TextView tvHour;
    CardView setHourBtn;
    EditText etMealName,
    etMealContent;
    TextView DoneBtn,CancelBtn;

    //Ctor
    public addMealFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
       View v = inflater.inflate(R.layout.fragment_add_meal,null);
       //init Views
       findViews(v);

       //Open Clock Dialog
       setHourBtn.setOnClickListener((c)->{
           SetTimePickerDialog();
       });

       DoneBtn.setOnClickListener((c->{
           if(Validation()){
               Meal meal = new Meal(etMealName.getText().toString(),etMealContent.getText().toString(),tvHour.getText().toString());
               addMeal(meal);
               dismiss();
           }
       }));

       CancelBtn.setOnClickListener((c->{
           dismiss();
       }));


        // Setting the View
       builder.setView(v).setTitle(getString(R.string.addmeal));

       return builder.create();
    }

    // Method init all views
    private void findViews(View v){
         tvHour = v.findViewById(R.id.tvAddMealHourBtnText);
        setHourBtn = v.findViewById(R.id.btnAddMealSetHour);
        etMealName = v.findViewById(R.id.etAddMealName);
        etMealContent = v.findViewById(R.id.etMealContent);
        DoneBtn = v.findViewById(R.id.AddMealDoneText);
        CancelBtn = v.findViewById(R.id.addMealCancelText);
    }

    //Method that Open Clock Dialog
    private void SetTimePickerDialog(){
        int Hour;
        int Mins;
        Calendar c = Calendar.getInstance();
        Hour = c.get((Calendar.HOUR));
        Mins = c.get((Calendar.MINUTE));
        TimePickerDialog dialog;
        dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute < 10){
                    tvHour.setText(hourOfDay+":0"+minute);
                } else tvHour.setText(hourOfDay+":"+minute);
            }
        },Hour,Mins,true);
        dialog.show();
    }

    // Method Validating Dialog
    private boolean Validation(){
        if(tvHour.getText().toString().equals(getString(R.string.sethour))){
            tvHour.setError(getString(R.string.hourrequired));
        }
        if(etMealContent.getText().length() == 0){
            etMealContent.setError(getString(R.string.contentrequired));
        }
        if(etMealName.getText().length() == 0){
            etMealName.setError(getString(R.string.namerequired));
        }

        return !tvHour.getText().toString().equals(getString(R.string.sethour)) && etMealName.getText().length() != 0 && etMealContent.getText().length() != 0;
    }


    //Method Adding the Meal to Database
    private void addMeal(Meal meal){
        //save
        FirebaseDatabase.getInstance().getReference().child("meals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(meal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(DoneBtn, e.getMessage() , Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
