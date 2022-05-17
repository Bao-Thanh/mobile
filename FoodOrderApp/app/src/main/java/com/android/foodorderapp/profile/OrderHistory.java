package com.android.foodorderapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.foodorderapp.MainActivity;
import com.android.foodorderapp.RestaurantMenuActivity;
import com.android.foodorderapp.ui.profile.ProfileFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.android.foodorderapp.R;
import com.android.foodorderapp.adapters.OrderListApdater;
import com.android.foodorderapp.adapters.RestaurantListAdapter;
import com.android.foodorderapp.model.Orders;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistory extends AppCompatActivity {
    RecyclerView recycle_order_history;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    OrderListApdater myApdapter;
    View itemView;
    ArrayList<Orders> list = new ArrayList<Orders>();
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        //Add action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lịch sử mua hàng");
        //find view
        recycle_order_history = findViewById(R.id.recycler_order_history);
        itemView = findViewById(R.id.cardView);
        //Get current id
        //get firebase auth instance
        //get current user
        firebaseAuth = FirebaseAuth.getInstance();
        userid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        //get database
        mDatabase = FirebaseDatabase.getInstance().getReference("orders").child(userid);
        recycle_order_history.setHasFixedSize(true);
        recycle_order_history.setLayoutManager(new LinearLayoutManager(this));
        myApdapter = new OrderListApdater(this, list);
        recycle_order_history.setAdapter(myApdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Orders order = dataSnapshot.getValue(Orders.class);
                    if (order.getIsFinish() == 0) {
                        list.add(order);
                    }
                }
                myApdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivityForResult(new Intent(OrderHistory.this, ProfileFragment.class), 1000);
        finish();
    }
}
