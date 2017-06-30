package com.example.heramb.applicationoutline.Search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    private static final String TAG = "SearchListActivity";

    private String location, service;
    private Context mContext;
    private ProgressBar progressBar;
    private TextView progressBarText;
    private TextView noResults;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseSearch;

    private ListView listViewResults;
    private List<SearchResultItems> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        mContext = SearchListActivity.this;

        setUpFirebaseAuth();
        init();


    }
    private void init(){
        listViewResults= (ListView) findViewById(R.id.searchResultList);
        resultList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.searchListProgressBar);
        progressBarText = (TextView) findViewById(R.id.searchListProgressBarText);
        noResults = (TextView) findViewById(R.id.searchListNoResults);

        noResults.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            service = extras.getString("service");
            location = extras.getString("location");
            Log.d(TAG, "Service:" + service + "Location:" + location);
        }
        databaseSearch = FirebaseDatabase.getInstance().getReference(location).child(service);
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

        databaseSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultList.clear();
                for (DataSnapshot providerSnapshot : dataSnapshot.getChildren()) {
                    SearchResultItems results = providerSnapshot.getValue(SearchResultItems.class);
                    resultList.add(results);
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
