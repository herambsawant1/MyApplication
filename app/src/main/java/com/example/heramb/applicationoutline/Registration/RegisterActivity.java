package com.example.heramb.applicationoutline.Registration;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Context mContext;

    private Button registerButton;
    private EditText emailText, userNameText, passwordText, confirmPasswordText;
    private TextView signInText;
    private ProgressBar progressBar;
    private TextView progressBarText;
    //Local strings
    private String username,password,email,confirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: started.");

        setUpWidgets();
        setupFirebaseAuth();
        init();
    }

    private void setUpWidgets(){
        registerButton = (Button) findViewById(R.id.registerSignUpButton);
        emailText = (EditText) findViewById(R.id.registerEmail);
        userNameText = (EditText) findViewById(R.id.registerUsername);
        passwordText = (EditText) findViewById(R.id.registerPassword);
        confirmPasswordText = (EditText) findViewById(R.id.registerConfirmPassword);
        signInText = (TextView) findViewById(R.id.registerSignInText);
        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        progressBarText = (TextView) findViewById(R.id.registerProgressBarText);

        progressBar.setVisibility(View.GONE);
        progressBarText.setVisibility(View.GONE);
    }
    private void init(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                username = userNameText.getText().toString();
                password = passwordText.getText().toString();
                confirmPassword = confirmPasswordText.getText().toString();

                if(checkInputs(email, username, password, confirmPassword)){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBarText.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, username);
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        progressBarText.setVisibility(View.GONE);
    }
    private boolean checkInputs(String email, String username, String password, String confirmPassword){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("") || confirmPassword.equals("")){
            Toast.makeText(mContext, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(mContext, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

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

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //add new user to the database
                            firebaseMethods.addNewUser(email, username, "", "");
                            progressBar.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Signup successful", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
