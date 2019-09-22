package me.ofir.fitme.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ofir.fitme.Entites.User;
import me.ofir.fitme.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateUsFragment extends AppCompatDialogFragment {

    RatingBar ratingBar;
    TextView tvMsg;
    TextView btnCancel;
    TextView btnSend;
    FirebaseUser user;

    public RateUsFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_rate_us, null);
        builder.setTitle("");
        builder.setView(v);
        findViews(v);


        btnSend.setOnClickListener((c->{
            sendRating(ratingBar,tvMsg);
        }));

        btnCancel.setOnClickListener((c->{
            dismiss();
        }));


        return builder.create();
    }

    private void findViews(View v) {
        ratingBar = v.findViewById(R.id.ratingBar);
        btnSend = v.findViewById(R.id.tvRateSend);
        btnCancel = v.findViewById(R.id.tvRateCancel);
        tvMsg = v.findViewById(R.id.tvRateMsg);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void sendRating(RatingBar rb,TextView msg){
        float rating = rb.getRating();


        msg.setText(String.valueOf(rating));
        setAnimation(msg);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                u.setUserRating(rating);
                dataSnapshot.getRef().setValue(u).addOnSuccessListener((s->{
                    btnSend.postDelayed(()->{
                        dismiss();
                    },1500);
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAnimation(TextView msg){
        Animation anim = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in);
        anim.setDuration(2000);
        msg.setAnimation(anim);
    }

}
