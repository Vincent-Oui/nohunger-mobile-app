package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class donate_history_donate_id extends AppCompatActivity {
    TextView a,b,c,d,id2;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        String txt1 = getIntent().getStringExtra("DONATEID");
        String jaja = getIntent().getStringExtra("USERID");
        id2 = (TextView)findViewById(R.id.textView21);
        ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton6);
        Log.i("tag",getIntent().getStringExtra("DONATEID"));
        String txt2 = getIntent().getStringExtra("status");
        id2.setText(txt1);
        Log.i("tag",txt2);
        if(txt2.equals("restaurant")){
            Log.i("tag","passr1");
            setrestaurant(txt1,jaja);
        }
        else if(txt2.equals("individual")){
            setindividual(txt1,jaja);
        }
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(donate_history_donate_id.this, settings_donate_history.class));
                finish();
            }
        });
    }

    public void setrestaurant(String t, String z){
        Log.i("tag","passr");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Restaurant").child(z).child(t);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a = (TextView)findViewById(R.id.textView26);
                b = (TextView)findViewById(R.id.textView27);
                c = (TextView)findViewById(R.id.textView29);
                d = (TextView)findViewById(R.id.textView31);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String addresss = dataSnapshot.child("restaurantAddress").getValue().toString();
                    String Foodname = dataSnapshot.child("restaurantFoodName").getValue().toString();
                    String FoodQuantity = dataSnapshot.child("restaurantFoodQuantity").getValue().toString();
                    String Name = dataSnapshot.child("restaurantName").getValue().toString();
                    a.setText(addresss);
                    b.setText(Foodname);
                    c.setText(FoodQuantity+" pack(s)");
                    d.setText(Name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setindividual(String t, String z){
        Log.i("tag","passr");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Individual").child(z).child(t);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a = (TextView)findViewById(R.id.textView26);
                b = (TextView)findViewById(R.id.textView27);
                c = (TextView)findViewById(R.id.textView29);
                d = (TextView)findViewById(R.id.textView31);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String addresss = dataSnapshot.child("individualAddress").getValue().toString();
                    String Foodname = dataSnapshot.child("individualFoodName").getValue().toString();
                    String FoodQuantity = dataSnapshot.child("individualFoodQuantity").getValue().toString();
                    String Name = dataSnapshot.child("individualName").getValue().toString();
                    a.setText(addresss);
                    b.setText(Foodname);
                    c.setText(FoodQuantity+" pack(s)");
                    d.setText(Name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}