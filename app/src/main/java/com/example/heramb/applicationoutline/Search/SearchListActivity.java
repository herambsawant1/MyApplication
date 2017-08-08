package com.example.heramb.applicationoutline.Search;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heramb.applicationoutline.Models.UserInformation;
import com.example.heramb.applicationoutline.Profile.ProfileActivity;
import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Registration.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    private static final String TAG = "SearchListActivity";

    private String location, service;
    private Context mContext;
    private ProgressBar progressBar;
    private TextView progressBarText, noResults;
    private ImageView backImage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseSearch;

    private ListView listViewResults;
    private List<UserInformation> resultList;
    private String selectedUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        mContext = SearchListActivity.this;

        setUpFirebaseAuth();
        init();

        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub

                UserInformation user = (UserInformation) parent.getItemAtPosition(position);
                getKey(user.getUsername());
            }
        });

    }
    private void init(){
        listViewResults= (ListView) findViewById(R.id.searchResultList);
        resultList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.searchListProgressBar);
        progressBarText = (TextView) findViewById(R.id.searchListProgressBarText);
        noResults = (TextView) findViewById(R.id.searchListNoResults);
        backImage = (ImageView) findViewById(R.id.resultsBackArrow);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noResults.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            service = extras.getString("service");
            location = extras.getString("location");
            Log.d(TAG, "Service:" + service + "Location:" + location);
        }

        databaseSearch = FirebaseDatabase.getInstance().getReference(getString(R.string.dbname_user_information));
    }

    private void getKey(final String userName){
        String id;
        databaseSearch.orderByChild(getString(R.string.field_location)).equalTo(location).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot providerSnapshot : dataSnapshot.getChildren()) {
                    UserInformation results = providerSnapshot.getValue(UserInformation.class);
                    if(results.getUsername().equals(userName)) {
                        selectedUserID = providerSnapshot.getKey();
                        Intent intent = new Intent(mContext, SearchProfileActivity.class);
                        intent.putExtra("UID", selectedUserID);
                        startActivity(intent);
                    }
                }
                ServiceProvidersList adaptor = new ServiceProvidersList(SearchListActivity.this, resultList);
                if(resultList.size() == 0){
                    noResults.setVisibility(View.VISIBLE);
                }
                listViewResults.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Firebase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

        databaseSearch.orderByChild(getString(R.string.field_location)).equalTo(location).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultList.clear();
                for (DataSnapshot providerSnapshot : dataSnapshot.getChildren()) {
                    UserInformation results = providerSnapshot.getValue(UserInformation.class);
                    providerSnapshot.getKey();
                    if(results.getService().equals(service)) {
                        resultList.add(results);
                        Log.d(TAG, "Added " + results.getDisplayName() + " to the search results");
                    }
                }
                progressBar.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                ServiceProvidersList adaptor = new ServiceProvidersList(SearchListActivity.this, resultList);
                if(resultList.size() == 0){
                    noResults.setVisibility(View.VISIBLE);
                }
                listViewResults.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
