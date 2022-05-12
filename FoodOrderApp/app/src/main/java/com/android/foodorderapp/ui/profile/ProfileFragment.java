package com.android.foodorderapp.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.android.foodorderapp.extras.LogoutDialog;

import com.android.foodorderapp.MainActivity;
import com.android.foodorderapp.extras.LogoutDialog;
import com.android.foodorderapp.profile.AboutApp;
import com.android.foodorderapp.profile.ContactDev;
import com.android.foodorderapp.profile.OrderHistory;
import com.android.foodorderapp.profile.PaymentHistory;
import com.android.foodorderapp.profile.ProfileCustomer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//com
import com.android.foodorderapp.LoginAccount;
import com.android.foodorderapp.R;

import java.util.Objects;

public class ProfileFragment extends Activity {
    private TextView cname, homepageProfile;
    private LinearLayout logoutProfile, aboutappProfile, profileCustomer,
            contactDev, orderHistory, paymentHistory;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    View h, p ,nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        //Find view
        cname = findViewById(R.id.profileName);
        homepageProfile = findViewById(R.id.homepageProfile);
        logoutProfile = findViewById(R.id.linearl7);
        aboutappProfile = findViewById(R.id.linearl6);
        profileCustomer = findViewById(R.id.linearl0);
        contactDev = findViewById(R.id.linearl5);
        orderHistory = findViewById(R.id.linearl3);
        paymentHistory = findViewById(R.id.linearl4);
        h = findViewById(R.id.navigation_home);
        p = findViewById(R.id.navigation_profile);
        //
        p.setBackgroundColor(0xFF66ccff);
        //Checked signed
        //Firebase
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        DocumentReference typeref = db.collection("users").document(userid);
        typeref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname");
                    cname.setText(fullName);
                }
            }
        });

        //event click
        //redirected homepage
        homepageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, MainActivity.class);
                startActivity(i);
                //startActivity(new Intent(MainActivity.this, ProfileFragment.class));
                finish();
            }
        });

        //loggout
        logoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(ProfileFragment.this, LoginAccount.class);
                startActivity(intent);
                finish();
            }
        });

        //about app
        aboutappProfile.setOnClickListener((v) -> {
            Intent i = new Intent(ProfileFragment.this, AboutApp.class);
            startActivity(i);
        });

        //profile
        profileCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, ProfileCustomer.class);
                startActivity(i);
            }
        });

        //Contact our team
        contactDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, ContactDev.class);
                startActivity(i);
            }
        });

        //Order history
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, OrderHistory.class);
                startActivity(i);
            }
        });

        //Payment history
        paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, PaymentHistory.class);
                startActivity(i);
            }
        });

        //profile again
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        //navigator to home
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileFragment.this, MainActivity.class);
                startActivity(i);
            }
        });


        //-end
    }



}
