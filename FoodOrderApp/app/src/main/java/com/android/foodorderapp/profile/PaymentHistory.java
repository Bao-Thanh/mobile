package com.android.foodorderapp.profile;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.R;
import com.android.foodorderapp.ViewOrderDetail;
import com.android.foodorderapp.adapters.OrderListApdater;
import com.android.foodorderapp.model.Orders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentHistory extends AppCompatActivity {
    RecyclerView recycle_order_history;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    OrderListApdater myApdapter;
    View itemView;
    ArrayList<Orders> list = new ArrayList<Orders>();
    private String userid;
    private int isDone = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        //Add action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Danh sách đã thanh toán");

        //find view
        recycle_order_history = findViewById(R.id.recycler_payment_history);
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
                    if (order.getIsFinish() == 1) {
                        isDone = 1;
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
}
