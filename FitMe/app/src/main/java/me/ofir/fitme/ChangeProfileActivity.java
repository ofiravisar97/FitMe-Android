package me.ofir.fitme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Entites.User_Profile;

public class ChangeProfileActivity extends AppCompatActivity {
    EditText Age,Kg,Cm;
    Button Done;
    User_Profile info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        getSupportActionBar().hide();
        FindViews();
        info = new User_Profile();
        getUserInfo(); // Getting the info Updated

        Done.setOnClickListener((c)->{ // On Click Set for Done Btn
            changeProfileInfo();
        });

    }

    private void FindViews(){
        Age = findViewById(R.id.ChangeProfileAge);
        Kg = findViewById(R.id.ChangeProfileKg);
        Cm = findViewById(R.id.ChangeProfileCm);
        Done = findViewById(R.id.ChangeProfileDoneBtn);
    } // Finiding Views

    /**
     * Method Setting Updated User Profile at Start
     */

    private void getUserInfo(){
        FirebaseDatabase.getInstance().getReference().child("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Profile profile = dataSnapshot.getValue(User_Profile.class);
                info.setMale(profile.isMale());
                info.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that changing the User Info From Database
     */
    private void changeProfileInfo(){
        info.setAge(Integer.parseInt(Age.getText()+""));
        info.setKg(Integer.parseInt(Kg.getText()+""));
        info.setCm(Integer.parseInt(Cm.getText()+""));
        FirebaseDatabase.getInstance().getReference().child("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(info).addOnSuccessListener((v)->{
            Toast.makeText(this, getString(R.string.profile_success), Toast.LENGTH_SHORT).show();
           Done.postDelayed(()->{
               startActivity(new Intent(this,ProfileActivity.class));
           },1000);
        });
    }
}
