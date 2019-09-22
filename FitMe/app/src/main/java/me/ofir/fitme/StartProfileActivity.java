package me.ofir.fitme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Entites.User;
import me.ofir.fitme.Entites.User_Profile;

public class StartProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etAge,etKg,etCm;
    Button btnSetProfile;
    RadioGroup genderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_profile);
        getSupportActionBar().hide();
        findViews();

        btnSetProfile.setOnClickListener(this);

    }

    private boolean Validation(){
        if(etAge.getText().length() == 0) etAge.setError("Field Required");
        if(etKg.getText().length() == 0) etKg.setError("Field Required");
        if(etCm.getText().length() == 0) etCm.setError("Field Required");
        if(etAge.getText().length() == 0 || etCm.getText().length() == 0 || etKg.getText().length() == 0){
            return false;
        } else return true;
    }

    private void setProfile(){
        if(!Validation())return; // Checking Nulls
        if(genderGroup.getCheckedRadioButtonId() == R.id.rbMale){ // If MALE
            User_Profile userInfo = new User_Profile(Integer.parseInt(etAge.getText().toString()),Integer.parseInt(etKg.getText().toString()),Integer.parseInt(etCm.getText().toString()),true,FirebaseAuth.getInstance().getCurrentUser().getEmail());
            writeUserInfo(userInfo);
        } else { // If Female
            User_Profile userInfo = new User_Profile(Integer.parseInt(etAge.getText().toString()),Integer.parseInt(etKg.getText().toString()),Integer.parseInt(etCm.getText().toString()),false,FirebaseAuth.getInstance().getCurrentUser().getEmail());
            writeUserInfo(userInfo);
        }
    }

    private void findViews(){
         etAge = findViewById(R.id.etSetProfileAge);
         etKg = findViewById(R.id.etSetProfileKg);
         etCm = findViewById(R.id.etSetProfileCm);
         btnSetProfile = findViewById(R.id.SetProfileBtn);
         genderGroup = findViewById(R.id.genderGroup);
    }

    @Override
    public void onClick(View view) {
        setProfile();
    }

    private void writeUserInfo(User_Profile info){
        FirebaseDatabase.getInstance().getReference().child("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        u.setHaveProfile(true);
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(StartProfileActivity.this, "Profile setted.", Toast.LENGTH_SHORT).show();
                                btnSetProfile.postDelayed(()->{
                                    startActivity(new Intent(getApplicationContext(),ContentActivity.class));
                                },1500);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
