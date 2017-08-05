package com.example.heramb.applicationoutline.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heramb.applicationoutline.R;

/**
 * Created by heram on 8/3/2017.
 */

public class ChangeEmailFragment extends Fragment {

    private static final String TAG = "ChangeEmailFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_in, container, false);

        return view;
    }
}
