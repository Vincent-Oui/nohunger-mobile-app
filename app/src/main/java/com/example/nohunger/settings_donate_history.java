package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

public class settings_donate_history extends AppCompatActivity {
    private String userID;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Button button;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_id);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userID = mFirebaseUser.getUid();
        ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton7);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Restaurant");
        Log.i("tag","restaurant");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    checkrestaurant(userID);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        DatabaseReference dotRef = FirebaseDatabase.getInstance().getReference().child("Individual");
        Log.i("tag","indi");
        dotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    checkindividual(userID);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings_donate_history.this, settings.class));
                finish();
            }
        });
    }

    public void checkrestaurant(String i){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Restaurant").child(i);
        db.addValueEventListener(new ValueEventListener() {
            String kullaniciad= "";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    kullaniciad=data.child("restaurantID").getValue().toString();
                    addButton(kullaniciad,"restaurant", userID);
                    Log.i("tag",kullaniciad);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkindividual(String i){
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Individual").child(i);
        db.addValueEventListener(new ValueEventListener() {
            String kullaniciad= "";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    kullaniciad=data.child("individualID").getValue().toString();
                    addButton(kullaniciad,"individual", userID);
                    Log.i("tag",kullaniciad);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addButton(String r, String g, String h){
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear1);
        button = new Button(this);
        button.setText(r);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(settings_donate_history.this, donate_history_donate_id.class);
                intent.putExtra("USERID", h);
                intent.putExtra("DONATEID",r);
                intent.putExtra("status",g);
                Log.i("tag",r);
                Log.i("tag",g);
                startActivity(intent);
                finish();
            }
        });
        layout.addView(button);
    }
}