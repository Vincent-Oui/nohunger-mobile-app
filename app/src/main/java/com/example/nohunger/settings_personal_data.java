package com.example.nohunger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class settings_personal_data extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore firebaseFirestore;
    TextView textView, textView2;
    ImageView imageView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        textView = (TextView)findViewById(R.id.editTextTextPersonName2);
        textView2 = (TextView)findViewById(R.id.editTextTextPersonName);
        imageView = (ImageView)findViewById(R.id.imageView3);

        if (mFirebaseUser.getPhotoUrl() != null){
            String photoUrl = mFirebaseUser.getPhotoUrl().toString();
            photoUrl = photoUrl + "?type=large";
            Picasso.get().load(photoUrl).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.default_profile2);
        }

        if (mFirebaseUser != null){
            textView.setText(mFirebaseUser.getEmail());
            textView2.setText(mFirebaseUser.getDisplayName());

            if (mFirebaseUser != null){
                textView2.setText(mFirebaseUser.getDisplayName());
                String l = mFirebaseUser.getEmail();
                if(mFirebaseUser.getDisplayName().equals("")){
                    DocumentReference documentReference = firebaseFirestore.collection("Other User").document(l);
                    documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value.exists()){
                                textView2.setText(value.getString("Name"));
                            }else {
                                Log.d("tag", "onEvent: Document do not exists");
                            }
                        }
                    });
                }
            }
        }

        ImageButton btn2 = (ImageButton) findViewById(R.id.imageButton5);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings_personal_data.this, settings.class));
                finish();
            }
        });
    }
}