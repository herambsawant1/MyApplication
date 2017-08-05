package com.example.heramb.applicationoutline.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Registration.SignInActivity;
import com.example.heramb.applicationoutline.Utils.BottomNavigationViewHelper;
import com.example.heramb.applicationoutline.Utils.FirebaseMethods;
import com.example.heramb.applicationoutline.Utils.UniversalImageLoader;
import com.example.heramb.applicationoutline.models.User;
import com.example.heramb.applicationoutline.models.UserCombinedInfo;
import com.example.heramb.applicationoutline.models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by heram on 8/2/2017.
 */

public class ProfileHomeFragment extends Fragment {

    private static final String TAG = "ProfileHomeFragment";
    private static final int ACTIVITY_NUM = 2;

    private TextView profileUserName, profileName, profileLocation, profileAvail, profileService, profileBio, serviceRatingCount;
    private RatingBar serviceRating, experienceRating;
    private ProgressBar progressBar;
    private CircleImageView profilePhoto;
    private Button mail, request, gallery;
    private Toolbar toolbar;
    private ImageView options;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_profile_home, container, false);
        setupWidgets(view);
        mContext = getActivity();
        setupBottomNavigationView();
        setupToolbar();
        setUpFirebaseAuth();
        return view;
    }
    private void setupWidgets(View view){
        profileUserName = (TextView) view.findViewById(R.id.profileName);
        profileName = (TextView) view.findViewById(R.id.profileInfoName);
        profileLocation = (TextView) view.findViewById(R.id.profileInfoLocation);
        profileAvail = (TextView) view.findViewById(R.id.profileInfoAvail);
        profileService = (TextView) view.findViewById(R.id.profileInfoService);
        profileBio = (TextView) view.findViewById(R.id.profileInfoDescription);
        serviceRating = (RatingBar) view.findViewById(R.id.profileInfoServiceRating);
        experienceRating = (RatingBar) view.findViewById(R.id.profileInfoExperienceRating);
        progressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        profilePhoto = (CircleImageView) view.findViewById(R.id.profileImage);
        mail = (Button) view.findViewById(R.id.profileInfoMail);
        request = (Button) view.findViewById(R.id.profileInfoRequest);
        gallery = (Button) view.findViewById(R.id.profileGalleryImages);
        options = (ImageView) view.findViewById(R.id.profileMenu);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        serviceRatingCount = (TextView) view.findViewById(R.id.profileInfoServiceRatingCount);
        mFirebaseMethods = new FirebaseMethods(getActivity());
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
    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`

    private void setUpFirebaseAuth(){
        Log.d(TAG, "Setting up firebase authentication");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
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
                populateWidgets(mFirebaseMethods.getUserAccountSettings(dataSnapshot));

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
