package me.ofir.fitme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import me.ofir.fitme.Entites.Calculator;
import me.ofir.fitme.Entites.User;
import me.ofir.fitme.Entites.User_Profile;

public class ProfileActivity extends AppCompatActivity {

    User_Profile currentUser;
    TextView tvName;
    TextView tvAge,tvKg,tvCm;
    TextView BMR,RMR,BMI;
    ImageView ivGender;
    CardView changeProfileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        findViews();
        getProfile();
        changeProfileBtn.setOnClickListener((c)->{
            startActivity(new Intent(this,ChangeProfileActivity.class));
        });
    }

    private void findViews(){
        tvName = findViewById(R.id.tvProfileName);
        tvAge = findViewById(R.id.tvProfileAge);
        tvKg = findViewById(R.id.tvProfileKg);
        tvCm = findViewById(R.id.tvProfileCm);
        ivGender = findViewById(R.id.ivGender);
        BMR = findViewById(R.id.BMRText);
        RMR = findViewById(R.id.RMRText);
        BMI = findViewById(R.id.BMIText);
        changeProfileBtn = findViewById(R.id.ChangeProfileBtn);
    }

    private void getProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("profiles").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User_Profile.class);
                setUI(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUI(User_Profile info){
        Calculator c = new Calculator();
        tvName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        tvAge.setText(info.getAge()+"");
        tvKg.setText((info.getKg()+""));
        tvCm.setText(info.getCm()+"");

        if(info.isMale()){ // USER IS MALE
            ivGender.setImageResource(R.drawable.male);
            calculateMan(info.getAge(),info.getKg(),info.getCm(),c);
        } else { // USER IS FEMALE
            ivGender.setImageResource(R.drawable.femenine);
            calculateWoman(info.getAge(),info.getKg(),info.getCm(),c);
        }
        BMI.setText(getString(R.string.your_bmi) + c.bmiCalculate(info.getKg(),info.getCm()));
    }

    private void calculateMan(int age,int kg,int cm,Calculator c){
        BMR.setText(getString(R.string.your_bmr) + c.bmrMale(kg,cm,age));
        RMR.setText(getString(R.string.your_rmr) + c.rmrMale(kg,cm,age));
    }

    private void calculateWoman(int age,int kg,int cm,Calculator c){
         BMR.setText(getString(R.string.your_bmr)+c.bmrFemale(kg,cm,age));
        RMR.setText(getString(R.string.your_rmr)+ c.rmrFemale(kg,cm,age));
    }
}
