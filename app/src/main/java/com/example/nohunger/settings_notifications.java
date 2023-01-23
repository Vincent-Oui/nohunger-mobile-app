package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class settings_notifications extends AppCompatActivity {
    private Switch switch1,switch2;
    public static final String SWITCH1 = "switch1";
    public static final String SWITCH2 = "switch2";
    private Button saveButton;
    private boolean switchOnOff1,switchOnOff2;
    public static final String SHARED_PREFS = "sharedPrefs";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton4);
        saveButton = (Button) findViewById(R.id.button2);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        loadData();
        updateViews();
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings_notifications.this, settings.class));
                finish();
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.putBoolean(SWITCH2, switch2.isChecked());
        editor.apply();
        Toast.makeText(this, "Preferences Saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff1 = sharedPreferences.getBoolean(SWITCH1, false);
        switchOnOff2 = sharedPreferences.getBoolean(SWITCH2, false);
    }

    public void updateViews() {
        switch1.setChecked(switchOnOff1);
        switch2.setChecked(switchOnOff2);
    }
}