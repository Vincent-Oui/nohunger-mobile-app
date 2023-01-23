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
import com.example.nohunger.Object.RestaurantDonate;
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

public class Restaurant extends AppCompatActivity {
    private ArrayAdapter<String> lvAdapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    String userId = mFirebaseUser.getUid();
    TextInputEditText etRestaurantAddress,etRestaurantName, etRestaurantFoodName;
    Button btnRestaurantAdd, btnRestaurantMinus, btnRestaurantConfirm;
    TextView tvRestaurantQuantity;
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
    List<RestaurantDonate> restaurantDonates;
    DatabaseReference databaseRestaurants;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etRestaurantAddress = findViewById(R.id.et_restaurant_address);
        etRestaurantName = findViewById(R.id.et_restaurant_name);
        etRestaurantFoodName = findViewById(R.id.et_restaurant_foodname);
        btnRestaurantAdd = findViewById(R.id.btn_restaurant_add);
        btnRestaurantMinus = findViewById(R.id.btn_restaurant_minus);
        btnRestaurantConfirm = findViewById(R.id.btn_restaurant_confirm);
        imageView = findViewById(R.id.imageView6);
        tvRestaurantQuantity = findViewById(R.id.tv_restaurant_quantity);
        lvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        imggps = findViewById(R.id.img_gps2);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Restaurant.this);

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
                    Toast.makeText(Restaurant.this, "Sorry, there is no application can handle this action and data.", Toast.LENGTH_LONG).show();
                }
            }
        });
        databaseRestaurants = FirebaseDatabase.getInstance().getReference("Restaurant").child(userId);
        restaurantDonates = new ArrayList<>();
        databaseRestaurants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot staffDataSnapshot : dataSnapshot.getChildren()) {
                    RestaurantDonate restaurantDonate = staffDataSnapshot.getValue(RestaurantDonate.class);
                    restaurantDonates.add(restaurantDonate);
                    lvAdapter.add("Restaurant ID: " + restaurantDonate.getRestaurantID() + "\n" +"Restaurant Address: " + restaurantDonate.getRestaurantAddress() + "\n" + "Restaurant Name: " + restaurantDonate.getRestaurantName() + "\n" +
                            "Food Name: " + restaurantDonate.getRestaurantFoodName()  + "\n" + "Quantity: " + restaurantDonate.getRestaurantFoodQuantity());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnRestaurantConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextIsEmpty()) {
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Restaurant.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Please make sure the information you provided is valid.");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String RestaurantAddress = etRestaurantAddress.getText().toString().trim();
                        String RestaurantName = etRestaurantName.getText().toString().trim();
                        String RestaurantFoodName = etRestaurantFoodName.getText().toString().trim();
                        Integer RestaurantFoodQuantity = Integer.valueOf(tvRestaurantQuantity.getText().toString().trim());
                        if (!TextUtils.isEmpty(RestaurantAddress) && !TextUtils.isEmpty(RestaurantName) && !TextUtils.isEmpty(RestaurantFoodName)){
                            String Restaurantid  = databaseRestaurants.push().getKey();
                            RestaurantDonate newRestaurantDonate = new RestaurantDonate(Restaurantid,RestaurantAddress, RestaurantName,RestaurantFoodQuantity, RestaurantFoodName);
                            databaseRestaurants.child(Restaurantid).setValue(newRestaurantDonate);
                            etRestaurantAddress.setText("");
                            etRestaurantName.setText("");
                            etRestaurantFoodName.setText("");
                            Toast.makeText(Restaurant.this,"You had made a donation, rider is coming to pick it up", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(Restaurant.this, RestaurantDetails.class);
                        intent.putExtra("Restaurant Address", RestaurantAddress);
                        intent.putExtra("Restaurant Name", RestaurantName);
                        intent.putExtra("Restaurant Food Name", RestaurantFoodName);
                        intent.putExtra("Restaurant Quantity", RestaurantFoodQuantity);
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
                Intent intent = new Intent(Restaurant.this, donate_paths.class);
                startActivity(intent);
                finish();
            }
        });

        btnRestaurantAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                tvRestaurantQuantity.setText(""+quantity);
            }
        });

        btnRestaurantMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity--;
                if(quantity<=1){
                    quantity = 1;
                }
                tvRestaurantQuantity.setText(""+quantity);
            }
        });
    }

    private boolean editTextIsEmpty(){
        if (TextUtils.isEmpty(etRestaurantAddress.getText().toString())){
            etRestaurantAddress.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etRestaurantName.getText().toString())){
            etRestaurantName.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etRestaurantFoodName.getText().toString())){
            etRestaurantFoodName.setError("Cannot be Empty !");
        }
        if (TextUtils.isEmpty(etRestaurantAddress.getText().toString())||TextUtils.isEmpty(etRestaurantName.getText().toString())||TextUtils.isEmpty(etRestaurantFoodName.getText().toString())){
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
        client = LocationServices.getSettingsClient(Restaurant.this);
        task = client.checkLocationSettings(mLocationSettingsBuilder.build());
        task.addOnSuccessListener(Restaurant.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(Restaurant.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException)e;
                        resolvable.startResolutionForResult(Restaurant.this,REQUEST_CHECK_SETTING);
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
        if (ContextCompat.checkSelfPermission(Restaurant.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Restaurant.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation();
            } else {
                ActivityCompat.requestPermissions(Restaurant.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallBack,null);
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant.this);
        builder.setTitle("Requires Location Permission");
        builder.setMessage("This app needs location permission to get the location information.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(Restaurant.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
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
                        Toast.makeText(Restaurant.this, "Location Setting has Turned On",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED :
                        break;
                }
        }
    }
}