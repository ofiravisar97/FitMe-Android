package me.ofir.fitme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Login Screen
    EditText etEmail, etPassword;
    TextView registerText;
    Button btnLogin;
    ProgressBar pb;
    FrameLayout f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().hide();
        findViews();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,ContentActivity.class));
        }

        btnLogin.setOnClickListener((c) -> {
            hideKeyboard();
            if (!Validation()) { // Checking for Validation
                return;
            }
            if (!haveInternet()) { // Checking for Network
                openNetworkDialog(); // Opening Network Dialog
                return;
            }
            Login(); // Login
        });

        registerText.setOnClickListener((c) -> { // Moving to Registration Fragment
           openRegisterFragment();
        });
    }

    private void findViews() {
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        registerText = findViewById(R.id.tvRegister);
        pb = findViewById(R.id.pbLogin);
        f = findViewById(R.id.frame);
    } // Finding Views

    private void Login() {
        pb.setVisibility(View.VISIBLE);
        if (Validation()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    etEmail.getText().toString(),
                    etPassword.getText().toString()
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.login_sc), Toast.LENGTH_SHORT).show();
                    btnLogin.postDelayed(()->{
                        startActivity(new Intent(getApplicationContext(),ContentActivity.class));
                    },1000);
                }
            }).addOnFailureListener((e)->{
                pb.setVisibility(View.GONE);
                Snackbar.make(btnLogin, e.getMessage(), Snackbar.LENGTH_LONG).show();
            });
        }
    } // Login Method

    private boolean Validation() {
        if (etEmail.length() == 0) {
            etEmail.setError("Email Required");
        }
        if (etPassword.length() < 8) {
            etPassword.setError("Password must contain atleast 8 characters");
        }

        if (!isEmailValid(etEmail.getText().toString())) {
            etEmail.setError("Invalid email");
        }

        if (etPassword.length() == 0 || etEmail.length() == 0) {
            return false;
        } else return true;
    } // Method Checking for Validation and Declaring Validation Errors

    private boolean isEmailValid(String email) {
        Pattern emailRegex = Patterns.EMAIL_ADDRESS;
        return emailRegex.matcher(email).matches();
    } // Checking Email Pattern Validation

    private boolean haveInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    } // Checking if User have Network

    private void openNetworkDialog() {
        //Making No Internet Dialog
        AlertDialog builder;
        new AlertDialog.Builder(this).setTitle("No Network")
                .setMessage("Please try to connect your Internet")
                .setPositiveButton("Try", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setIcon(R.drawable.ic_wifi_black_24dp).show();
    } // Opening get network Dialog

    private void openRegisterFragment(){
            f.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.frame, new RegisterFragment(this)).commit();
    }

    private void hideKeyboard(){
        View v = this.getCurrentFocus();
        if(v != null){
            InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

}
