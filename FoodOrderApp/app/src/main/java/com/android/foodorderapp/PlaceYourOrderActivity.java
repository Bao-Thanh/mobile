package com.android.foodorderapp;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlaceYourOrderActivity extends AppCompatActivity {

    private EditText inputName, inputAddress, inputCity, inputState, inputNumberAddress,inputCardNumber, inputPhno, inputEmail ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    //for data binding
    private String userid;
    private String sfirstname, slastname, semail, sphno, sstk, sfullname;
    //For order;
    List<Menu> cmenu = new ArrayList<Menu>();
    private String dateTime, orderAt;
    String uniqueorderID;
    float atotalprice, subTotal;
    float adelivery = 0;
    String typedelivery, otp, image;
    //Firebase
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);
        //Add action bar
        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);
        //create date;
        Calendar calender= Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat(" EEEE, dd-MM-yyyy hh:mm:ss a");
        dateTime = simpleDateFormat.format(calender.getTime());
        //find view
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
                    inputAddress.setText("Vi???t Nam");
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
                    buttonPlaceYourOrder.setText("Giao h??ng!");
                    inputAddress.setVisibility(View.VISIBLE);
                    inputCity.setVisibility(View.VISIBLE);
                    inputState.setVisibility(View.VISIBLE);
                    inputNumberAddress.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                    calculateTotalAmount(restaurantModel);
                } else {
                    buttonPlaceYourOrder.setText("?????t th???c ??n!");
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


    //Convert to VND format
    public String toVND(float value) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(value);
    }

    private void calculateTotalAmount(RestaurantModel restaurantModel) {
        orderAt = restaurantModel.getName();
        image = restaurantModel.getImage();
        cmenu = restaurantModel.getMenus();
        float subTotalAmount = 0f;
        int i = 0;
        for(Menu m : restaurantModel.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }
        subTotal = subTotalAmount;
        tvSubtotalAmount.setText(toVND(subTotalAmount) + " VND");
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText(toVND(restaurantModel.getDelivery_charge()) +" VND");
            subTotalAmount += restaurantModel.getDelivery_charge();

        }
        atotalprice = subTotalAmount;
        adelivery = restaurantModel.getDelivery_charge();
        tvTotalAmount.setText(toVND(subTotalAmount) + " VND");
    }

    private void onPlaceOrderButtonClick(RestaurantModel restaurantModel) {
        //Khi ng?????i d??ng nh???n ?????t h??ng th?? show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("X??c nh???n mua h??ng")
                .setMessage("B???n c?? ch???c ch???n mu???n mua h??ng?")
                .setCancelable(false)
                .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // here you can add functions
                        if(isDeliveryOn && TextUtils.isEmpty(inputCity.getText().toString())) {
                            inputCity.setError("Th??nh ph??? ");
                            return;
                        }else if(isDeliveryOn && TextUtils.isEmpty(inputState.getText().toString())) {
                            inputState.setError("Qu???n/Huy???n/TP");
                            return;
                        }else if(isDeliveryOn && TextUtils.isEmpty(inputNumberAddress.getText().toString())) {
                            inputNumberAddress.setError("S??? nha");
                            return;
                        }
                        else if( TextUtils.isEmpty(inputCardNumber.getText().toString())) {
                            inputCardNumber.setError("S??? th??? ");
                            return;
                        }else if( TextUtils.isEmpty(inputPhno.getText().toString())) {
                            inputPhno.setError("S??? ??i???n tho???i li??n k???t v???i s??? th???");
                            return;
                        }else if( TextUtils.isEmpty(inputEmail.getText().toString())) {
                            inputEmail.setError("Email li??n k???t v???i s??? th???");
                            return;
                        }
                        //start success activity..
                        //Add order detail in to Database
                        uniqueorderID = UUID.randomUUID().toString();
                        int random_int = (int)Math.floor(Math.random()*1000000);
                        otp = String.valueOf(random_int);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("orders").child(userid);
                        //set status delivery
                        if (!isDeliveryOn) { typedelivery = "Placed"; } else { typedelivery = "Delivered";}
                        //Create object
                        Orders order = new Orders(uniqueorderID, userid, sfullname,
                                sphno, semail,
                                inputCity.getText().toString(),
                                inputState.getText().toString(),
                                inputNumberAddress.getText().toString(),
                                sstk,orderAt, image, cmenu, adelivery,
                                atotalprice, typedelivery, dateTime, otp, subTotal, 0);


                        //push order
                        mDatabase.child(uniqueorderID).setValue(order);

                        //
                        Intent i = new Intent(PlaceYourOrderActivity.this, OrderSucceessActivity.class);
                        i.putExtra("RestaurantModel", restaurantModel);
                        startActivityForResult(i, 1000);


                    }
                })
                .setNegativeButton("Hu??? b???", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

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