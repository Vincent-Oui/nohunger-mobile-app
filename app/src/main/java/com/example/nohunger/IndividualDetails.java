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

public class IndividualDetails extends AppCompatActivity {
    Button btnIndividualDonateMore, btnHome;
    TextView tvIndividualDetailsAddress, tvIndividualDetailsName, tvIndividualDetailsFoodname, tvIndividualDetailsQuantity;
    String IndividualAddress, IndividualName, IndividualFoodName;
    Integer IndividualFoodQuantity;
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
        setContentView(R.layout.activity_individual_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnIndividualDonateMore = findViewById(R.id.btn_individual_donate_more);
        tvIndividualDetailsAddress = findViewById(R.id.tv_individual_details_address);
        tvIndividualDetailsName = findViewById(R.id.tv_individual_details_name);
        tvIndividualDetailsFoodname = findViewById(R.id.tv_individual_details_foodname);
        tvIndividualDetailsQuantity = findViewById(R.id.tv_individual_details_quantity);
        Intent intent = getIntent();
        IndividualAddress = intent.getStringExtra("Individual Address");
        IndividualName = intent.getStringExtra("Individual Name");
        IndividualFoodName = intent.getStringExtra("Individual Food Name");
        IndividualFoodQuantity = intent.getIntExtra("Individual Quantity",0);
        tvIndividualDetailsAddress.setText(IndividualAddress);
        tvIndividualDetailsName.setText(IndividualName);
        tvIndividualDetailsFoodname.setText(IndividualFoodName);
        tvIndividualDetailsQuantity.setText(IndividualFoodQuantity+" pack(s)");

        btnIndividualDonateMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualDetails.this, donate_paths.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome = findViewById(R.id.button7);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        shareButton = findViewById(R.id.sb_fb2);
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setQuote("I (" + IndividualName + ") have donated " + IndividualFoodQuantity + " pack(s) of " + IndividualFoodName + " via NoHunger. Let's do our part for the community !")
                .setContentUrl(Uri.parse("https://borgenproject.org/7-facts-about-hunger-in-malaysia/"))
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("NoHunger").build()).build();
        shareButton.setShareContent(shareLinkContent);
    }
}
