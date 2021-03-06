package com.android.foodorderapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.adapters.PlaceYourOrderAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.Orders;
import com.android.foodorderapp.model.RestaurantModel;
import com.android.foodorderapp.profile.OrderHistory;
import com.android.foodorderapp.profile.PaymentHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class ViewOrderDetail extends AppCompatActivity {
    private EditText inputName, inputAddress, inputCity, inputState, inputNumberAddress,inputCardNumber, inputPhno, inputEmail ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvIsFinish, tvSubtotalAmount, tvDeliveryChargeAmount, tvOtp, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private String userid;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    //Firebase
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth;
    DatabaseReference mDatabase;
    //List menu
    ArrayList<Menu> listMenu = new ArrayList<Menu>();
    //Get extra
    private String isFinish, orderid, sfname, stp, sqh, ssn, sstk, sphno, semail, sstatus, sotp, ssub, stotal, sdeli;

    //For payment
    private TextView txtOtp, desciptotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        //find view
        inputName = findViewById(R.id.inputName);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputState = findViewById(R.id.inputState);
        inputNumberAddress = findViewById(R.id.inputNumberAddress);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputPhno = findViewById(R.id.inputPhno);
        inputEmail = findViewById(R.id.inputEmail);
        tvIsFinish = findViewById(R.id.isFinish);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        switchDelivery = findViewById(R.id.switchDelivery);
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder = findViewById(R.id.received);
        //for payment
        tvOtp = findViewById(R.id.sotp);
        txtOtp = findViewById(R.id.textOtp);
        desciptotp = findViewById(R.id.descriptotp);
        //Get intent extra and binding
        isFinish = getIntent().getStringExtra("isFinish");
        orderid = getIntent().getStringExtra("orderid");
        userid = getIntent().getStringExtra("userid");
        sstatus = getIntent().getStringExtra("status");
        sfname = getIntent().getStringExtra("fname");
        stp = getIntent().getStringExtra("thanhpho");
        sqh = getIntent().getStringExtra("quanhuyen");
        ssn = getIntent().getStringExtra("sonha");
        sstk = getIntent().getStringExtra("sstk");
        sphno = getIntent().getStringExtra("phno");
        semail = getIntent().getStringExtra("email");
        sotp = getIntent().getStringExtra("otp");
        sdeli = getIntent().getStringExtra("fdelivery");
        ssub = getIntent().getStringExtra("subTotal");
        stotal = getIntent().getStringExtra("totalPrice");
        //Binding in view
        if (isFinish.equals("1")) {
            tvIsFinish.setVisibility(View.VISIBLE);
            txtOtp.setVisibility(View.GONE);
            tvOtp.setVisibility(View.GONE);
            desciptotp.setVisibility(View.GONE);
            buttonPlaceYourOrder.setVisibility(View.GONE);
        }
        inputAddress.setText("Vi???t Nam");
        inputName.setText(sfname);
        inputCity.setText(stp);
        inputState.setText(sqh);
        inputNumberAddress.setText(ssn);
        inputCardNumber.setText(sstk);
        inputPhno.setText(sphno);
        inputEmail.setText(semail);
        tvSubtotalAmount.setText(toVND(Float.parseFloat(ssub)) + " VND");
        tvTotalAmount.setText(toVND(Float.parseFloat(stotal)) + " VND");
        if (sstatus.equals("Placed")) {
            //nothing
        } else {
            tvDeliveryChargeAmount.setText(toVND(Float.parseFloat(sdeli)) + " VND");
        }
        tvOtp.setText(sotp);

        //Get real menu data
        mDatabase = FirebaseDatabase.getInstance().getReference("orders").child(userid).child(orderid).child("menu");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    listMenu.add(menu);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Switch button T??? l???y - Giao h??ng
        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    inputAddress.setVisibility(View.VISIBLE);
                    inputCity.setVisibility(View.VISIBLE);
                    inputState.setVisibility(View.VISIBLE);
                    inputNumberAddress.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                } else {
                    inputAddress.setVisibility(View.GONE);
                    inputCity.setVisibility(View.GONE);
                    inputState.setVisibility(View.GONE);
                    inputNumberAddress.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    isDeliveryOn = false;
                }
            }
        });
        initRecyclerView(listMenu);
        ////////////////Placed
        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("orders").child(userid).child(orderid).child("isFinish").setValue(1);
                Toast.makeText(ViewOrderDetail.this, "\uD83C\uDF89 Congrats! Enjoy the Meal \uD83E\uDD73", Toast.LENGTH_SHORT).show();
                //Delay v?? chuy???n sang trang danh s??ch ???? thanh to??n
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ViewOrderDetail.this, PaymentHistory.class));

                        finish();
                    }
                }, 3000);

            }
        });
    }

    //Convert to VND format
    public String toVND(float value) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(value);
    }

    //Adapter for menu
    private void initRecyclerView(List<Menu> lMenu) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(lMenu);
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    public void onBackPressed() {
        //check n???u ??ang ??? trang view c???a payment th?? h???i c??n k th?? tr???c ti???p cho lui l???i
        if (isFinish.equals("1")) {
            startActivity(new Intent(ViewOrderDetail.this, PaymentHistory.class));
            finish();
        }
        else {
            startActivity(new Intent(ViewOrderDetail.this, OrderHistory.class));
            finish();
        }

    }
}
