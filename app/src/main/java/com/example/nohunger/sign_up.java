package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FirebaseFirestore";
    private ImageButton imgBtnBack;
    private TextInputEditText tietName, tietEmail, tietPassword;
    private Button btnSignUp;
    private ProgressBar pbLoading;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        imgBtnBack = findViewById(R.id.img_btn_back_sign_up);
        tietName = findViewById(R.id.tiet_name_sign_up);
        tietEmail = findViewById(R.id.tiet_email_sign_up);
        tietPassword = findViewById(R.id.tiet_password_sign_up);
        btnSignUp = findViewById(R.id.btn_sign_up_sign_up);
        pbLoading = findViewById(R.id.pb_loading_sign_up);

        if (mFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this, sign_in.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tietName.getText().toString().trim();
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString();
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    tietName.setError("Name is Required !");
                    tietEmail.setError("Email is Required !");
                    tietPassword.setError("Password is Required !");
                    return;
                } else if (TextUtils.isEmpty(name)){
                    tietName.setError("Name is Required !");
                    return;
                } else if (TextUtils.isEmpty(email)){
                    tietEmail.setError("Email is Required !");
                    return;
                } else if (TextUtils.isEmpty(password)){
                    tietPassword.setError("Password is Required !");
                    return;
                }
                if (password.length() < 8){
                    tietPassword.setError("Password Must be At Least 8 Characters !");
                    return;
                }
                pbLoading.setVisibility(View.VISIBLE);
                mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(sign_up.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = firebaseFirestore.collection("Other User").document(email);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",name);
                            user.put("Email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: User Profile is created for " + email);
                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user","other");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(sign_up.this, "Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();
                            pbLoading.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}