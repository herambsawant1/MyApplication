package com.example.heramb.applicationoutline.Search;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.heramb.applicationoutline.Models.UserCombinedInfo;
import com.example.heramb.applicationoutline.Models.UserInformation;
import com.example.heramb.applicationoutline.Profile.AccountSettingsActivity;
import com.example.heramb.applicationoutline.Profile.ProfileActivity;
import com.example.heramb.applicationoutline.Profile.ProfileImagesActivity;
import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.BottomNavigationViewHelper;
import com.example.heramb.applicationoutline.Utils.FirebaseMethods;
import com.example.heramb.applicationoutline.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by heram on 8/7/2017.
 */

public class SearchProfileActivity extends AppCompatActivity {
    private static final String TAG = "SearchProfileActivity";

    private TextView profileUserName, profileName, profileLocation, profileAvail, profileService, profileBio, serviceRatingCount;
    private RatingBar serviceRating, experienceRating;
    private ProgressBar progressBar;
    private ImageView backImage;
    private CircleImageView profilePhoto;
    private Button mail, request, gallery;
    private Toolbar toolbar;
    private String userID;
    private Context mContext;
    private String userEmail;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);
        mContext = SearchProfileActivity.this;
        setupWidgets();
        setupToolbar();
        setUpFirebaseAuth();

    }

    private void setupWidgets(){
        profileUserName = (TextView) findViewById(R.id.profileName);
        profileName = (TextView) findViewById(R.id.profileInfoName);
        profileLocation = (TextView) findViewById(R.id.profileInfoLocation);
        profileAvail = (TextView) findViewById(R.id.profileInfoAvail);
        profileService = (TextView) findViewById(R.id.profileInfoService);
        profileBio = (TextView) findViewById(R.id.profileInfoDescription);
        serviceRating = (RatingBar) findViewById(R.id.profileInfoServiceRating);
        experienceRating = (RatingBar) findViewById(R.id.profileInfoExperienceRating);
        progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        profilePhoto = (CircleImageView) findViewById(R.id.profileImage);
        mail = (Button) findViewById(R.id.profileInfoMail);
        request = (Button) findViewById(R.id.profileInfoRequest);
        gallery = (Button) findViewById(R.id.profileGalleryImages);
        //options = (ImageView) findViewById(R.id.profileMenu);
        toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        serviceRatingCount = (TextView) findViewById(R.id.profileInfoServiceRatingCount);
        backImage = (ImageView) findViewById(R.id.profileBackArrow);
        mFirebaseMethods = new FirebaseMethods(mContext);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("UID");
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to gallery images.");
                Intent intent = new Intent(mContext, SearchProfileGalleryActivity.class);
                startActivity(intent);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void populateWidgets(UserCombinedInfo userCombinedInfo){
        UserInformation information = userCombinedInfo.getInformation();

        UniversalImageLoader.setImage(information.getProfilePhoto(), profilePhoto,null, "");
        profileUserName.setText(information.getUsername());
        profileName.setText(information.getDisplayName());
        profileLocation.setText(information.getLocation());
        profileService.setText(information.getService());
        profileBio.setText(information.getDescription());
        serviceRating.setRating(information.getServiceRating());
        experienceRating.setRating(information.getExperienceRating());
        serviceRatingCount.setText(String.valueOf(information.getExperienceRating()));
        progressBar.setVisibility(View.GONE);

    }

    public void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {userEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Service required");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "From some service app name: ");
        emailIntent.setType("message/rfc882");

        startActivity(emailIntent);
        Log.d(TAG, "Finished sending email...");
    }
    private void setupToolbar(){

//        ((SearchProfileActivity)getApplicationContext()).setSupportActionBar(toolbar);
//        options.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating to account settings.");
//                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`

    private void setUpFirebaseAuth(){
        Log.d(TAG, "Setting up firebase authentication");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userEmail = mAuth.getCurrentUser().getEmail();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                Log.d(TAG, "+++++++++++++++++" + userID);
                populateWidgets(mFirebaseMethods.getServiceProviderUserAccountSettings(dataSnapshot, userID));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
