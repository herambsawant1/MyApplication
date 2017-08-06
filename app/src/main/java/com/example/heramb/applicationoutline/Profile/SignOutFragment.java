package com.example.heramb.applicationoutline.Profile;

import android.content.Context;
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
import android.widget.ImageView;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Registration.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";

    private Button signoutButton;
    private ImageView backButton;

    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        init(view);
        setUpFirebaseAuth();

        return view;
    }

    private void init(View view){

        signoutButton = (Button) view.findViewById(R.id.signOutButton);
        backButton = (ImageView) view.findViewById(R.id.signOut_backArrow);
        mContext = getActivity();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
                Intent intent = new Intent(getContext(), AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "Navigating to register activity");
                FirebaseAuth.getInstance().signOut(); //End user session
                Intent intent = new Intent(mContext, SignInActivity.class); //Go back to home page
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
    private void verifyCurrentUser(FirebaseUser user){
        if(user == null){
            Intent intent = new Intent(mContext, SignInActivity.class);
            startActivity(intent);
        }
    }

    private void setUpFirebaseAuth(){
        Log.d(TAG, "Setting up firebase authentication");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                verifyCurrentUser(user);
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
