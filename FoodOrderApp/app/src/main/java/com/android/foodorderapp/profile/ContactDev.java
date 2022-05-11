package com.android.foodorderapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.foodorderapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class ContactDev extends AppCompatActivity {
    //for get and binding
    Spinner spinner;
    String severity,a,name;
    EditText username,problemdesc;
    Button sendmail;
    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developer);
        //call firebase
        fAuth = FirebaseAuth.getInstance();
        a = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //find view
        //snipper để toggle các trường hợp cho người dùng dễ chọn
        spinner = findViewById(R.id.severityspinner);
        username = findViewById(R.id.user_name);
        problemdesc = findViewById(R.id.problem_description);
        sendmail = findViewById(R.id.sendmail);
        //Thêm các trường hợp
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Rất nhẹ");
        arrayList.add("Nhẹ");
        arrayList.add("Trung bình");
        arrayList.add("Cao");
        arrayList.add("Dữ dội");
        arrayList.add("Cấp bách");
        //Adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ContactDev.this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //lấy thông tin của người dùng hiện tại
        DocumentReference typeref = db.collection("users").document(a);
        typeref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                    name = documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname");
                username.setText(String.format("%s", name));
            }
        });

        //Binding phần mức độ nghiêm trọng - mặc định là mức rất nhẹ
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                severity = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                severity ="Rất nhẹ";
            }
        });

        //Gửi email cho team
        sendmail.setOnClickListener((v) -> {
            //Lưu nội dung dòng text
            String desc = problemdesc.getText().toString().trim();

            if(TextUtils.isEmpty(desc)) {
                problemdesc.setError("Chi tiết vấn đề là cần thiết!");
                return;
            }
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            emailIntent.setType("message/rfc822");
            //emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"19110252@student.hcmute.edu.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Báo cáo mức độ nghiêm trọng: "+severity+" từ "+name);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, desc);
            startActivity(Intent.createChooser(emailIntent, "Chọn một ứng dụng để gửi email"));
        });
    }
}
