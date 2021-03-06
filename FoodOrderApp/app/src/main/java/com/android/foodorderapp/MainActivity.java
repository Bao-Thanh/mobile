package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.foodorderapp.adapters.RestaurantListAdapter;
import com.android.foodorderapp.extras.LogoutDialog;
import com.android.foodorderapp.model.RestaurantModel;
import com.android.foodorderapp.ui.profile.ProfileFragment;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.foodorderapp.extras.Broadcast;

import static com.android.foodorderapp.extras.Broadcast.IS_NETWORK_AVAILABLE;
import static com.android.foodorderapp.extras.Broadcast.NETWORK_AVAILABLE_ACTION;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    ImageButton btnSignOut, btnProfile;
    View h, p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Action when wifi change
        IntentFilter intentFilter = new IntentFilter(Broadcast.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                Snackbar.make(findViewById(R.id.activity_main), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, intentFilter);


        //Add action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        List<RestaurantModel> restaurantModelList =  getRestaurantData();
        //Slider show
        ImageSlider slider= findViewById(R.id.slider);
        List<SlideModel> sliderModels= new ArrayList<>();
        sliderModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzAjwWndhgbdClEpHJcKJWBB-bUB8mhfX8pg&usqp=CAU"));
        sliderModels.add(new SlideModel("https://i.etsystatic.com/14993754/r/il/9e92a7/1260487993/il_fullxfull.1260487993_b700.jpg"));
        sliderModels.add(new SlideModel("https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/f6500853536975.5937f720be83e.png"));
        sliderModels.add(new SlideModel("https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/1ab6c353536975.5937f720bee80.png"));
        sliderModels.add(new SlideModel("https://i.ytimg.com/vi/nVfE0G19KaI/maxresdefault.jpg"));
        slider.setImageList(sliderModels, true);

        //find button on action_bar
        btnSignOut = findViewById(R.id.btnLoggout);
        btnProfile = findViewById(R.id.btnProfile);
        h = findViewById(R.id.navigation_home);
        p = findViewById(R.id.navigation_profile);
        h.setBackgroundColor(0xFFccefff);
        //Recycle view for restaurant
        initRecyclerView(restaurantModelList);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null

                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginAccount.class));
                    finish();
                }
            }
        };

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create
                Intent i = new Intent(MainActivity.this, ProfileFragment.class);
                startActivity(i);
                //startActivity(new Intent(MainActivity.this, ProfileFragment.class));
                finish();
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileFragment.class);
                startActivity(i);

            }
        });

    }

    //Select
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnLoggout:
                openDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //For open dialog
    public void openDialog(){
        LogoutDialog logoutdialog = new LogoutDialog();
        logoutdialog.show(getSupportFragmentManager(),"Log out Dialog");
    }

    //Run
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    private void initRecyclerView(List<RestaurantModel> restaurantModelList ) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<RestaurantModel> getRestaurantData() {
        InputStream is = getResources().openRawResource(R.raw.restaurent);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try{
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while(( n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0,n);
            }
        }catch (Exception e) {

        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        RestaurantModel[] restaurantModels =  gson.fromJson(jsonStr, RestaurantModel[].class);
        List<RestaurantModel> restList = Arrays.asList(restaurantModels);

        return  restList;

    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
        Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("RestaurantModel", restaurantModel);
        startActivity(intent);

    }
}