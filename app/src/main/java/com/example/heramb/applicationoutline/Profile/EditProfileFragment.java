package com.example.heramb.applicationoutline.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;


public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;
    private Button monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private Boolean mondaySwitch, tuesdaySwitch, wednesdaySwitch, thursdaySwitch,
            fridaySwitch, saturdaySwitch, sundaySwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        Log.d(TAG, "onCreate: started.");


        setProfileImage();
//        setUpAvailButtons(view);

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

        return view;
    }
    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile image.");
        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");
    }
//    private void setUpAvailButtons(View view){
//        monday = (Button) view.findViewById(R.id.editProfileMonday);
//        mondaySwitch = false;
//        tuesdaySwitch = false;
//        wednesdaySwitch = false;
//        thursdaySwitch = false;
//        fridaySwitch = false;
//        saturdaySwitch = false;
//        sundaySwitch = false;
//
//        monday.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(!mondaySwitch){
//                    monday.setPressed(true);
//                    mondaySwitch = true;
//                    return true;
//                }
//                else{
//                    monday.setPressed(false);
//                    mondaySwitch = false;
//                    return true;
//                }
//            }
//        });
//    }
}
