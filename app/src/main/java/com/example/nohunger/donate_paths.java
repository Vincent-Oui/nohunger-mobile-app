package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class donate_paths extends AppCompatActivity {
    Button btnRestaurant, btnIndividual;
    ImageButton imageButton;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnRestaurant = findViewById(R.id.btn_restaurant);
        btnIndividual = findViewById(R.id.btn_individual);
        imageButton = findViewById(R.id.imageButton8);

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donate_paths.this, Restaurant.class);
                startActivity(intent);
            }
        });

        btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donate_paths.this, Individual.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donate_paths.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}