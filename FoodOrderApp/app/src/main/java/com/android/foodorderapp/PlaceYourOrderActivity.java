package com.android.foodorderapp;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.foodorderapp.adapters.PlaceYourOrderAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.Orders;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlaceYourOrderActivity extends AppCompatActivity {
    double randomOrderIds;
    int quantity;
    float aprice, atotalprice;
    String typedelivery, apicture;
    private EditText inputName, inputAddress, inputCity, inputState, inputNumberAddress,inputCardNumber, inputPhno, inputEmail ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    //for data binding
    private String userid;
    private String sfirstname, slastname, semail, sphno, sstk, sfullname;
    List<Menu> cmenu = new ArrayList<Menu>();
    //Firebase
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.inputName);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputState = findViewById(R.id.inputState);
        inputNumberAddress = findViewById(R.id.inputNumberAddress);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputPhno = findViewById(R.id.inputPhno);
        inputEmail = findViewById(R.id.inputEmail);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        //Get authentication and binding view
        fAuth = FirebaseAuth.getInstance();
        userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        DocumentReference typeref = db.collection("users").document(userid);
        typeref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    semail=documentSnapshot.getString("email");
                    sfirstname=documentSnapshot.getString("firstname");
                    slastname=documentSnapshot.getString("lastname");
                    sphno=documentSnapshot.getString("phno");
                    sstk=documentSnapshot.getString("numberaccount");
                    sfullname = sfirstname + " " + slastname;

                    //Set user infomation
                    inputName.setText(sfullname);
                    inputAddress.setText("Việt Nam");
                    inputCardNumber.setText(sstk);
                    inputPhno.setText(sphno);
                    inputEmail.setText(semail);
                }
            }
        });






        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(restaurantModel);
            }
        });

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
                    calculateTotalAmount(restaurantModel);
                } else {
                    inputAddress.setVisibility(View.GONE);
                    inputCity.setVisibility(View.GONE);
                    inputState.setVisibility(View.GONE);
                    inputNumberAddress.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    isDeliveryOn = false;
                    calculateTotalAmount(restaurantModel);
                }
            }
        });
        initRecyclerView(restaurantModel);
        calculateTotalAmount(restaurantModel);
    }


    //Convert
    public String toVND(float value) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(value);
    }

    private void calculateTotalAmount(RestaurantModel restaurantModel) {
        float subTotalAmount = 0f;
        int i = 0;
        for(Menu m : restaurantModel.getMenus()) {
            Menu smenu = new Menu(m.getName(), m.getPrice(),m.getTotalInCart(), m.getUrl());
            cmenu.add(smenu);
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }
        atotalprice = subTotalAmount;
        tvSubtotalAmount.setText(toVND(subTotalAmount) + " VND");
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText(toVND(restaurantModel.getDelivery_charge()) +" VND");
            subTotalAmount += restaurantModel.getDelivery_charge();
            atotalprice = subTotalAmount;
        }
        tvTotalAmount.setText(toVND(subTotalAmount) + " VND");
    }

    private void onPlaceOrderButtonClick(RestaurantModel restaurantModel) {
        if(isDeliveryOn && TextUtils.isEmpty(inputCity.getText().toString())) {
            inputCity.setError("Thành phố ");
            return;
        }else if(isDeliveryOn && TextUtils.isEmpty(inputState.getText().toString())) {
            inputState.setError("Quận/Huyện/TP");
            return;
        }else if(isDeliveryOn && TextUtils.isEmpty(inputNumberAddress.getText().toString())) {
            inputNumberAddress.setError("Số nha");
            return;
        }
        else if( TextUtils.isEmpty(inputCardNumber.getText().toString())) {
            inputCardNumber.setError("Số thẻ ");
            return;
        }else if( TextUtils.isEmpty(inputPhno.getText().toString())) {
            inputPhno.setError("Số điện thoại liên kết với số thẻ");
            return;
        }else if( TextUtils.isEmpty(inputEmail.getText().toString())) {
            inputEmail.setError("Email liên kết với số thẻ");
            return;
        }
        //start success activity..
        //Add order detail in to Database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("orders").child(userid);
        String uniqueorderID = UUID.randomUUID().toString();
        //set status delivery
        if (!isDeliveryOn) { typedelivery = "Get"; } else { typedelivery = "Delivery";}
        //Create object
        Orders order = new Orders(uniqueorderID, userid, sfullname,
                sphno, semail,
                inputCity.getText().toString(),
                inputState.getText().toString(),
                inputNumberAddress.getText().toString(),
                sstk, cmenu, atotalprice, typedelivery);


        //push order
        mDatabase.child(uniqueorderID).setValue(order);







        Intent i = new Intent(PlaceYourOrderActivity.this, OrderSucceessActivity.class);
        i.putExtra("RestaurantModel", restaurantModel);
        startActivityForResult(i, 1000);
    }

    private void initRecyclerView(RestaurantModel restaurantModel) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurantModel.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}