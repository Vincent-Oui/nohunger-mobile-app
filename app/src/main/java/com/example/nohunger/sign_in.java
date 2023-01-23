package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class sign_in extends AppCompatActivity {
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private LoginButton loginButtonFacebook;
    private static final String TAG = "FacebookAuthentication";
    private FirebaseFirestore firebaseFirestore;
    private TextView tvSignUp, tvForgetPassword;
    private Button btnSignIn;
    private TextInputEditText tietEmail, tietPassword;
    private ProgressBar pbLoading;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        tvSignUp = findViewById(R.id.tv_sign_up_sign_in);
        tvForgetPassword = findViewById(R.id.tv_forgot_password_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in_sign_in);
        tietEmail = findViewById(R.id.tiet_email_sign_in);
        tietPassword = findViewById(R.id.tiet_password_sign_in);
        loginButtonFacebook = findViewById(R.id.login_btn_facebook_sign_in);
        pbLoading = findViewById(R.id.pb_loading_sign_in);
        loginButtonFacebook.setReadPermissions("email","public_profile");

        if (mFirebaseAuth.getCurrentUser() != null){
            openNextPage();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    tietEmail.setError("Email is Required !");
                    tietPassword.setError("Password is Required !");
                    return;
                } else if (TextUtils.isEmpty(email)){
                    tietEmail.setError("Email is Required !");
                    return;
                } else if (TextUtils.isEmpty(password)){
                    tietPassword.setError("Password is Required !");
                    return;
                }
                pbLoading.setVisibility(View.VISIBLE);
                mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(sign_in.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(sign_in.this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show();
                            pbLoading.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Please enter your email to receive the reset password link.");
                passwordResetDialog.setView(resetMail, 50, 50, 50, 50);
                passwordResetDialog.setCancelable(true);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        try {
                            mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(sign_in.this, "Reset Password Link had Sent to Your Email", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(sign_in.this, "Error ! Please Insert a Valid Email !", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e){
                            Toast.makeText(sign_in.this, "Please Insert an Email !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                passwordResetDialog.create().show();
            }
        });
        loginButtonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_in.this, sign_up.class));
                finish();
            }
        });
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "sign in with credential: successful");
                    DocumentReference documentReference = firebaseFirestore.collection("Facebook User").document(mFirebaseAuth.getCurrentUser().getEmail());
                    Map<String,Object> user = new HashMap<>();
                    user.put("Name", mFirebaseAuth.getCurrentUser().getDisplayName());
                    user.put("Email", mFirebaseAuth.getCurrentUser().getEmail());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: User Profile is created for " + mFirebaseAuth.getCurrentUser().getEmail());
                        }
                    });
                    openNextPage();
                } else {
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Toast.makeText(sign_in.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openNextPage(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}