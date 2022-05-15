package com.android.foodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.foodorderapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText txtEmail, txtPassword, txtFname, txtLname, txtTel, txtAddress, txtStk;
    private String semail,spassword,sfirstname,slastname,sphno,saddress,sstk, userid;
    Button btnCreate;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //find
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        txtFname = findViewById(R.id.firstname);
        txtLname = findViewById(R.id.lastname);
        txtTel = findViewById(R.id.phno);
        txtAddress = findViewById(R.id.address);
        txtStk = findViewById(R.id.numberAccount);
        btnCreate = findViewById(R.id.createaccButton);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrive the strings
                semail= txtEmail.getText().toString().trim();
                spassword= txtPassword.getText().toString().trim();
                sfirstname= txtFname.getText().toString().trim();
                slastname= txtLname.getText().toString().trim();
                sphno= txtTel.getText().toString().trim();
                saddress= txtAddress.getText().toString().trim();
                sstk = txtStk.getText().toString().trim();

                //Check for empty input
                if(TextUtils.isEmpty(semail)){
                    Toast.makeText(getApplicationContext(),"Please enter the email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(spassword)){
                    Toast.makeText(getApplicationContext(),"Please enter the password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sfirstname)){
                    Toast.makeText(getApplicationContext(),"Please enter your First Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(slastname)){
                    Toast.makeText(getApplicationContext(),"Please enter your Last Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sphno)){
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(saddress)){
                    Toast.makeText(getApplicationContext(),"Please enter your address",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sstk)){
                    Toast.makeText(getApplicationContext(),"Please enter your account number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sphno.length()!=10){
                    txtTel.setError("Number must be of 10 digits");
                    return;
                }
                if(sstk.length()!=10){
                    txtStk.setError("Pincode must be of 10 digits");
                    return;
                }

                //Save Data to Firestore
                //saveData(semail,spassword,sfirstname,slastname,sphno,saddress,spincode);
                fAuth = FirebaseAuth.getInstance();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                btnCreate.setText("Creating ...");
                fAuth.createUserWithEmailAndPassword(semail, spassword).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("userid", userid);
//                            user.put("email", semail);
//                            user.put("firstname", sfirstname);
//                            user.put("lastname", slastname);
//                            user.put("phno", sphno);
//                            user.put("address", saddress);
//                            user.put("numberaccount", sstk);
                            User user = new User(userid, semail, spassword, sfirstname, slastname, sphno, saddress,sstk);


                            //NEW
                            db.collection("users").document(userid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        btnCreate.setText("Create Account");
                                        Toast.makeText(CreateAccount.this,"Registration Successful! Welcome to FoodApp", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(CreateAccount.this, LoginAccount.class);
                                        startActivity(i);
                                    } else{
                                        btnCreate.setText("Create Account");
                                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(CreateAccount.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            //OLD
                            //Users info = new Users(fullName,email,phone,userid); class with getter setter

                        }else {
                            Toast.makeText(CreateAccount.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
