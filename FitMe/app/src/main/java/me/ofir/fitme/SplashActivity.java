package me.ofir.fitme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    /**
     *  ----------- Splash Activity -----------
     */

    //Vars
    TextView title;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide(); // Hiding Tool bar
        //Finding Views
        title = findViewById(R.id.tvTitleSplash);
        pb = findViewById(R.id.pbSplash);
        pb.setVisibility(View.VISIBLE);

        //Moving to Login / Main
        title.postDelayed(()->{
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            pb.setVisibility(View.GONE);
            finish();
        },4500);
    }
}
