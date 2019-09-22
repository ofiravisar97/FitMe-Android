package me.ofir.fitme;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import me.ofir.fitme.Entites.User;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    //Vars
    EditText etRegEmail, etRegPass,etDisplayName;
    Button btnReg;
    ProgressBar pbReg;
    Context ctx;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public RegisterFragment(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        findViews(v);

        btnReg.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        hideKeyboard(ctx);
        Register();
    }

    // Finiding Views
    private void findViews(View v) {
        etRegEmail = v.findViewById(R.id.etRegEmail);
        etRegPass = v.findViewById(R.id.etRegPass);
        etDisplayName = v.findViewById(R.id.etDisplayName);
        btnReg = v.findViewById(R.id.btnReg);
        pbReg = v.findViewById(R.id.pbReg);
    }

    private void Register() {
            if(!Validation()){
                return;
            }
            if (!haveInternet()) {
            openNetworkDialog();
            return;
        }

        pbReg.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etRegEmail.getText()+"",etRegPass.getText()+"").addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Getting user
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Verification on Email
                    user.sendEmailVerification();
                    // Setting Display Name
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(etDisplayName.getText()+"").build();
                    user.updateProfile(profileChangeRequest);


                    User u = new User(user.getDisplayName(),user.getEmail(),user.getUid());
                    String user_id = user.getUid();

                    // Writing user and Lamda for Succes
                    FirebaseDatabase.getInstance().getReference().child("users").child(user_id).setValue(u).addOnSuccessListener((s)->{
                        Toast.makeText(getContext(), "Registration Success", Toast.LENGTH_SHORT).show();

                        btnReg.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getContext(),ContentActivity.class));
                            }
                        }, 1500);
                    });

                } else {
                    Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show(); // Toasting Fail Message
                }
            }
        });
    }

    /**
     * Registration Validation
     * @return True if Validation Succes and Setting Errors on Fail
     */
    private  boolean Validation() {
        if (etRegEmail.length() == 0) {
            etRegEmail.setError("Email Required");

        }
        if (etRegPass.length() < 8) {
            etRegPass.setError("Password must contain atleast 8 characters");

        }

        if (!isEmailValid(etRegEmail.getText().toString())) {
            etRegEmail.setError("Invalid email");

        }

        if(etDisplayName.getText().length() == 0 ){
            etDisplayName.setError("Display Name Required");

        }
        if (etRegPass.length() == 0 || etRegEmail.length() == 0 || !isEmailValid(etRegEmail.getText().toString()) || etRegPass.getText().toString().length() < 8 || etDisplayName.getText().length() == 0) {
            return false;
        } else return true;
    }

    private boolean isEmailValid(String email) {
        Pattern emailRegex = Patterns.EMAIL_ADDRESS;
        return emailRegex.matcher(email).matches();
    }

    private boolean haveInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }


    private void openNetworkDialog() {
        //Making No Internet Dialog
        AlertDialog builder;
        new AlertDialog.Builder(getContext()).setTitle("No Network")
                .setMessage("Please try to connect your Internet")
                .setPositiveButton("Try", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setIcon(R.drawable.ic_wifi_black_24dp).show();
    } // Opening get network Dialog

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
