package com.example.heramb.applicationoutline.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heramb.applicationoutline.R;

/**
 * Created by heram on 8/5/2017.
 */

public class UserGalleryFragment extends Fragment {
    private static final String TAG = "UserGalleryFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_gallery, container, false);
        Log.d(TAG, "onCreateView: started.");

        return view;
    }
}
