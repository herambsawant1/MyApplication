package com.example.heramb.applicationoutline.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.FirebaseMethods;
import com.example.heramb.applicationoutline.Models.User;
import com.example.heramb.applicationoutline.Models.UserCombinedInfo;
import com.example.heramb.applicationoutline.Models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;
    private EditText editDisplayName, editBio, editPhoneNumber;
    private TextView editPhoto;
    private Spinner editLocation, editService;

    //variables
    private String userID;
    private UserCombinedInfo userCombinedInfo;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Log.d(TAG, "onCreate: started.");
        init(view);
        setUpFirebaseAuth();
        setUpButtons(view);

        return view;
    }
    private void init(View view){
        editDisplayName = (EditText) view.findViewById(R.id.displayNameText);
        editBio = (EditText) view.findViewById(R.id.bioText);
        editPhoneNumber = (EditText) view.findViewById(R.id.editUserPhoneNumber);
        editPhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        editLocation = (Spinner) view.findViewById(R.id.editLocationSpinner);
        editService = (Spinner) view.findViewById(R.id.editServiceSpinner);
        mFirebaseMethods = new FirebaseMethods(getActivity());
    }
    private void setUpButtons(View view){
        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) view.findViewById(R.id.signOut_backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
                Intent intent = new Intent(getContext(), AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        Button saveChangesButton = (Button) view.findViewById(R.id.editeProfileSaveButton);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileChanges();
            }
        });

        TextView saveChangesText = (TextView) view.findViewById(R.id.saveChanges);
        saveChangesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileChanges();
            }
        });
    }

    private void saveProfileChanges(){
        final String displayName = editDisplayName.getText().toString();
        final String description = editBio.getText().toString();
        final long phoneNumber = Long.parseLong(editPhoneNumber.getText().toString());
        final String location = editLocation.getSelectedItem().toString();
        final String service = editService.getSelectedItem().toString();

        if (!(userCombinedInfo.getUser().getPhoneNumber() == phoneNumber)){
            mFirebaseMethods.updateUserInformation(null, null, null, null, phoneNumber);
        }
        if (!userCombinedInfo.getInformation().getDescription().equals(description)){
            mFirebaseMethods.updateUserInformation(null, null, null, description, 0);
        }
        Log.d(TAG, service + "++" + userCombinedInfo.getInformation().getService());
        if (!userCombinedInfo.getInformation().getService().equals(service)){
            mFirebaseMethods.updateUserInformation(null, null, service, null, 0);
        }
        Log.d(TAG, location + "++" + userCombinedInfo.getInformation().getLocation());
        if (!userCombinedInfo.getInformation().getLocation().equals(location)){
            mFirebaseMethods.updateUserInformation(null, location, null, null, 0);
        }
        if (!userCombinedInfo.getInformation().getDisplayName().equals(displayName)){
            mFirebaseMethods.updateUserInformation(displayName, null, null, null, 0);
        }
        Toast.makeText(getActivity(), "Your profile has been updated", Toast.LENGTH_SHORT).show();
    }

    private void populateWidgets(UserCombinedInfo userCombinedInformation){

        userCombinedInfo = userCombinedInformation;

        User user = userCombinedInformation.getUser();
        UserInformation information = userCombinedInformation.getInformation();

        //UniversalImageLoader.setImage(information.getProfile_photo(), profilePhoto,null, "");
        editDisplayName.setText(information.getDisplayName());
        //TODO: fix spinners so they show users current status
        //editLocation.setText(information.getLocation());
        //editService.setText(information.getService());
        editBio.setText(information.getDescription());
        editPhoneNumber.setText(String.valueOf(user.getPhoneNumber()));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`

    private void setUpFirebaseAuth(){
        Log.d(TAG, "Setting up firebase authentication");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

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
