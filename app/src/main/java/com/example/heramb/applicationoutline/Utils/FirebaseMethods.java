package com.example.heramb.applicationoutline.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Models.User;
import com.example.heramb.applicationoutline.Models.UserCombinedInfo;
import com.example.heramb.applicationoutline.Models.UserInformation;
import com.example.heramb.applicationoutline.Search.SearchResultItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heram on 6/28/2017.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }
    public void addNewUser(String email, String username, String description, String profile_photo){

        User user = new User( userID, email, StringManipulation.condenseUsername(username), 0);

        myRef.child("users").child(userID).setValue(user);

        UserInformation info = new UserInformation(
                description, username, profile_photo, StringManipulation.condenseUsername(username), "", "", 0, 0, 0
        );

        myRef.child(mContext.getString(R.string.dbname_user_information))
                .child(userID)
                .setValue(info);
    }
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: upadating email to: " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }

    public void updateUserInformation(String displayName, String location, String service, String description,
                                            long phoneNumber){
        Log.d(TAG, "updateUserInfo: updating name to: " + displayName + ", location to: " + location + ", service tp: "
                + service + ", description to: " + description +", phone number to: " + phoneNumber + "");

        if (displayName != null) {
            myRef.child(mContext.getString(R.string.dbname_user_information))
                    .child(userID)
                    .child(mContext.getString(R.string.field_displayName))
                    .setValue(displayName);
        }
        if (location != null) {
            myRef.child(mContext.getString(R.string.dbname_user_information))
                    .child(userID)
                    .child(mContext.getString(R.string.field_location))
                    .setValue(location);
        }
        if (service != null) {
            myRef.child(mContext.getString(R.string.dbname_user_information))
                    .child(userID)
                    .child(mContext.getString(R.string.field_service))
                    .setValue(service);
        }
        if (phoneNumber != 0) {
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phoneNumber))
                    .setValue(phoneNumber);
        }
        if (description != null) {
            myRef.child(mContext.getString(R.string.dbname_user_information))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
    }


    /**
     * Retrieves a list of users which match search results
     * @param dataSnapshot
     * @return
     */
    public ArrayList searchUsers(String location, String service, DataSnapshot dataSnapshot){
        Log.d(TAG, "Searching users matching location: " + location + " and service: " + service + " in the database");
        ArrayList<UserInformation> resultList = new ArrayList<>();

        for (DataSnapshot providerSnapshot : dataSnapshot.getChildren()) {
            UserInformation results = providerSnapshot.getValue(UserInformation.class);
            resultList.add(results);
        }

        return resultList;
    }
    /**
     * Retrieves the account settings for teh user currently logged in
     * @param dataSnapshot
     * @return
     */
    public UserCombinedInfo getUserAccountSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountInformation: retrieving user account settings from firebase.");


        UserInformation information  = new UserInformation();
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_information))){
                Log.d(TAG, "getUserAccountInformation: datasnapshot: " + ds);

                try{
                    information.setDisplayName(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getDisplayName()
                    );
                    information.setUsername(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getUsername()
                    );
                    information.setDescription(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getDescription()
                    );
                    information.setProfilePhoto(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getProfilePhoto()
                    );
                    information.setExperienceRating(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getExperienceRating()
                    );
                    information.setServiceRating(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getServiceRating()
                    );
                    information.setServiceRatingCount(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getServiceRatingCount()
                    );
                    information.setDescription(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getDescription()
                    );
                    information.setService(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getService()
                    );
                    information.setLocation(
                            ds.child(userID)
                                    .getValue(UserInformation.class)
                                    .getLocation()
                    );


                    Log.d(TAG, "getUserAccountInformation: retrieved user_account_information: " + information.toString());
                }catch (NullPointerException e){
                    Log.e(TAG, "getUserAccountInformation: NullPointerException: " + e.getMessage() );
                }
            }
            // users node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountInformation: datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );
                user.setPhoneNumber(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhoneNumber()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserCombinedInfo(user, information);
    }
}
