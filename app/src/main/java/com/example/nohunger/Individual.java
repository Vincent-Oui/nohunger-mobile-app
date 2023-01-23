package com.example.nohunger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.nohunger.Object.IndividualDonate;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Individual extends AppCompatActivity {
    private ArrayAdapter<String> lvAdapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    String userId = mFirebaseUser.getUid();
    TextInputEditText etIndividualAddress, etIndividualName, etIndividualFoodName;
    Button btnIndividualAdd, btnIndividualMinus, btnIndividualConfirm;
    TextView tvIndividualQuantity;
    ImageView imageView, imggps;
    int quantity = 1;
    String lan, lon;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallBack;
    LocationSettingsRequest.Builder mLocationSettingsBuilder;
    SettingsClient client;
    Task<LocationSettingsResponse> task;
    private static final int REQUEST_CHECK_SETTING = 0x1;
    List<IndividualDonate> individualDonates;
    DatabaseReference databaseIndividual;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etIndividualAddress = findViewById(R.id.et_individual_address);
        etIndividualName = findViewById(R.id.et_individual_name);
        etIndividualFoodName = findViewById(R.id.et_individual_foodname);
        btnIndividualAdd = findViewById(R.id.btn__individual_add);
        btnIndividualMinus = findViewById(R.id.btn_individual_minus);
        btnIndividualConfirm = findViewById(R.id.btn_individual_confirm);
        imageView = findViewById(R.id.imageView11);
        tvIndividualQuantity = findViewById(R.id.tv_individual_quantity);
        lvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        imggps = findViewById(R.id.img_gps);
        databaseIndividual = FirebaseDatabase.getInstance().getReference("Individual").child(userId);
        individualDonates = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Individual.this);

        mLocationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                } else {
                    lan = Double.toString(locationResult.getLastLocation().getLatitude());
                    lon = Double.toString(locationResult.getLastLocation().getLongitude());
                }
            }
        };
        setLocationRequestSetting();

        imggps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:" + lan + "," + lon);
                Intent intent = new Intent(Intent.ACTION_VIEW, location);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(Individual.this, "Sorry, there is no application can handle this action and data.", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseIndividual.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot staffDataSnapshot : dataSnapshot.getChildren()) {
                    IndividualDonate individualDonate = staffDataSnapshot.getValue(IndividualDonate.class);
                    individualDonates.add(individualDonate);
                    lvAdapter.add("Individual ID: " + individualDonate.getIndividualID() + "\n" +"Individual Address: " + individualDonate.getIndividualAddress() + "\n" + "Individual Name: " + individualDonate.getIndividualName() + "\n" +
                            "Food Name: " + individualDonate.getIndividualFoodName() + "\n" + "Quantity: " + individualDonate.getIndividualFoodQuantity() + "Pack");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnIndividualConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextIsEmpty()) {
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Individual.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Please make sure the information you provided is valid.");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String IndividualAddress = etIndividualAddress.getText().toString().trim();
                        String IndividualName = etIndividualName.getText().toString().trim();
                        String IndividualFoodName = etIndividualFoodName.getText().toString().trim();
                        Integer IndividualFoodQuantity = Integer.valueOf(tvIndividualQuantity.getText().toString().trim());
                        if (!TextUtils.isEmpty(IndividualAddress) && !TextUtils.isEmpty(IndividualName) && !TextUtils.isEmpty(IndividualFoodName)){
                            String individualid = databaseIndividual.push().getKey();
                            IndividualDonate newIndividualDonate = new IndividualDonate(individualid, IndividualAddress, IndividualName,IndividualFoodQuantity, IndividualFoodName);
                            databaseIndividual.child(individualid).setValue(newIndividualDonate);
                            etIndividualAddress.setText("");
                            etIndividualName.setText("");
                            etIndividualFoodName.setText("");
                            Toast.makeText(Individual.this,"You had made a donation, rider is coming to pick it up", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(Individual.this, IndividualDetails.class);
                        intent.putExtra("Individual Address", IndividualAddress);
                        intent.putExtra("Individual Name", IndividualName);
                        intent.putExtra("Individual Food Name", IndividualFoodName);
                        intent.putExtra("Individual Quantity", IndividualFoodQuantity);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.create().show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Individual.this, donate_paths.class);
                startActivity(intent);
                finish();
            }
        });

        btnIndividualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                tvIndividualQuantity.setText(""+quantity);
            }
        });

        btnIndividualMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity--;
                if(quantity<=1){
                    quantity = 1;
                }
                tvIndividualQuantity.setText(""+quantity);
            }
        });
    }

    private boolean editTextIsEmpty(){
        if (TextUtils.isEmpty(etIndividualAddress.getText().toString())){
            etIndividualAddress.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etIndividualName.getText().toString())){
            etIndividualName.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etIndividualFoodName.getText().toString())){
            etIndividualFoodName.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etIndividualAddress.getText().toString())||TextUtils.isEmpty(etIndividualName.getText().toString())||TextUtils.isEmpty(etIndividualFoodName.getText().toString())){
            return true;
        }else
            return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallBack);
        }
    }

    private void requestLocationUpdate() {
        mLocationSettingsBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        client = LocationServices.getSettingsClient(Individual.this);
        task = client.checkLocationSettings(mLocationSettingsBuilder.build());
        task.addOnSuccessListener(Individual.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(Individual.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException)e;
                        resolvable.startResolutionForResult(Individual.this,REQUEST_CHECK_SETTING);
                    } catch (IntentSender.SendIntentException Ex) {
                    }
                }
            }
        });
    }

    private void setLocationRequestSetting() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(Individual.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Individual.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation();
            } else {
                ActivityCompat.requestPermissions(Individual.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallBack,null);
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Individual.this);
        builder.setTitle("Requires Location Permission");
        builder.setMessage("This app needs location permission to get the location information.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(Individual.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTING :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        Toast.makeText(Individual.this, "Location Setting has Turned On",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED :
                        break;
                }
        }
    }
}