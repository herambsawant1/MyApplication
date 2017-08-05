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

import com.example.heramb.applicationoutline.Dialogs.ConfirmPassword;
import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.FirebaseMethods;
import com.example.heramb.applicationoutline.Utils.UniversalImageLoader;
import com.example.heramb.applicationoutline.models.User;
import com.example.heramb.applicationoutline.models.UserCombinedInfo;
import com.example.heramb.applicationoutline.models.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class EditProfileFragment extends Fragment implements
        ConfirmPassword.OnConfirmPasswordListener{

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;
    private EditText editDisplayName, editBio, editEmail, editPhoneNumber;
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
        editEmail = (EditText) view.findViewById(R.id.editUserEmail);
        editPhoneNumber = (EditText) view.findViewById(R.id.editUserPhoneNumber);
        editPhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        editLocation = (Spinner) view.findViewById(R.id.editLocationSpinner);
        editService = (Spinner) view.findViewById(R.id.editServiceSpinner);
        mFirebaseMethods = new FirebaseMethods(getActivity());
    }
    private void setUpButtons(View view){
        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) view.findViewById(R.id.account_backArrow);
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
        final String email = editEmail.getText().toString();
        final long phoneNumber = Long.parseLong(editPhoneNumber.getText().toString());
        final String location = editLocation.getSelectedItem().toString();
        final String service = editService.getSelectedItem().toString();

        if (!userCombinedInfo.getUser().getEmail().equals(email)){
            ConfirmPassword dialog = new ConfirmPassword();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
            dialog.setTargetFragment(EditProfileFragment.this, 1);
        }
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

    private void checkIfEmailExists(final String email){
        Log.d(TAG, "checkIfEmailExists: Checking if  " + email + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_email))
                .equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //add the username
                    mFirebaseMethods.updateEmail(email);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfEmailExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That email already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        editEmail.setText(user.getEmail());
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

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: got the password: " + password);

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        ///////////////////// Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User re-authenticated.");

                            ///////////////////////check to see if the email is not already present in the database
                            mAuth.fetchProvidersForEmail(editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if(task.isSuccessful()){
                                        try{
                                            if(task.getResult().getProviders().size() == 1){
                                                Log.d(TAG, "onComplete: that email is already in use.");
                                                Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Log.d(TAG, "onComplete: That email is available.");

                                                //////////////////////the email is available so update it
                                                mAuth.getCurrentUser().updateEmail(editEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                                                                    mFirebaseMethods.updateEmail(editEmail.getText().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                        }catch (NullPointerException e){
                                            Log.e(TAG, "onComplete: NullPointerException: "  +e.getMessage() );
                                        }
                                    }
                                }
                            });





                        }else{
                            Log.d(TAG, "onComplete: re-authentication failed.");
                            Toast.makeText(getActivity(), "The password entered was incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
