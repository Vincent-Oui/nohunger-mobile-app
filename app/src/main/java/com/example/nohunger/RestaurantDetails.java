package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

public class RestaurantDetails extends AppCompatActivity {
    Button btnRestaurantDonateMore, btnHome;
    TextView tvRestaurantDetailsAddress, tvRestaurantDetailsName, tvRestaurantDetailsFoodname, tvRestaurantDetailsQuantity;
    String RestaurantAddress, RestaurantName, RestaurantFoodName;
    Integer RestaurantFoodQuantity;
    CallbackManager callbackManager;
    ShareButton shareButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnRestaurantDonateMore = findViewById(R.id.btn_restaurant_donate_more);
        tvRestaurantDetailsAddress = findViewById(R.id.tv_restaurant_details_address);
        tvRestaurantDetailsName = findViewById(R.id.tv_restaurant_details_name);
        tvRestaurantDetailsFoodname = findViewById(R.id.tv_restaurant_details_foodname);
        tvRestaurantDetailsQuantity = findViewById(R.id.tv_restaurant_details_quantity);
        Intent intent = getIntent();
        RestaurantAddress = intent.getStringExtra("Restaurant Address");
        RestaurantName = intent.getStringExtra("Restaurant Name");
        RestaurantFoodName = intent.getStringExtra("Restaurant Food Name");
        RestaurantFoodQuantity = intent.getIntExtra("Restaurant Quantity",0);
        tvRestaurantDetailsAddress.setText(RestaurantAddress);
        tvRestaurantDetailsName.setText(RestaurantName);
        tvRestaurantDetailsFoodname.setText(RestaurantFoodName);
        tvRestaurantDetailsQuantity.setText(RestaurantFoodQuantity+" pack(s)");

        btnRestaurantDonateMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetails.this, donate_paths.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome = findViewById(R.id.button9);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        shareButton = findViewById(R.id.sb_fb2);
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setQuote("I (" + RestaurantName + ") have donated " + RestaurantFoodQuantity + " pack(s) of " + RestaurantFoodName + " via NoHunger. Let's do our part for the community !")
                .setContentUrl(Uri.parse("https://borgenproject.org/7-facts-about-hunger-in-malaysia/"))
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("NoHunger").build()).build();
        shareButton.setShareContent(shareLinkContent);
    }
}