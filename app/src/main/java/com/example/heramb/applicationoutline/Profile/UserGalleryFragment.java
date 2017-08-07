package com.example.heramb.applicationoutline.Profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.heramb.applicationoutline.R;
import com.example.heramb.applicationoutline.Utils.FilePaths;
import com.example.heramb.applicationoutline.Utils.FileSearch;
import com.example.heramb.applicationoutline.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by heram on 8/5/2017.
 */

public class UserGalleryFragment extends Fragment {
    private static final String TAG = "UserGalleryFragment";
    private static final int NUM_OF_COLUMNS = 3;

    private ImageView selectedPicture, nextImage, cancelImage;
    private GridView userGallery;
    private Spinner directorySpinner;
    private ProgressBar progressBar;

    //variables
    private ArrayList<String> directories;
    private String appendFilePrefix = "file:/";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_gallery, container, false);
        Log.d(TAG, "onCreateView: started.");
        init(view);
        initSpinner();

        return view;
    }

    private void init(View view){
        selectedPicture = (ImageView) view.findViewById(R.id.galleryImage_CurrentImage);
        nextImage = (ImageView) view.findViewById(R.id.topGallery_share);
        cancelImage = (ImageView)view.findViewById(R.id.topGallery_back);
        directorySpinner = (Spinner) view.findViewById(R.id.topGallery_spinner);
        userGallery = (GridView) view.findViewById(R.id.galleryImage_Gallery);
        progressBar = (ProgressBar) view.findViewById(R.id.galleryimage_progressBar);
        directories = new ArrayList<>();

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Closing Actovity");
                getActivity().finish();
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigating to confirm sharing image fragment");
            }
        });
    }

    private void initSpinner(){
        FilePaths filePaths = new FilePaths();

        //check for other folders indide "/storage/emulated/0/pictures"
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
            Log.d(TAG, directories.toString());
        }
        directories.add(filePaths.CAMERA);

        //trim the url so it just say the current directory name
        //remove empty directories
        ArrayList<String> directoryNames = new ArrayList<>();
        directoryNames = fixDirectories(directoryNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Selected Directory: " + directories.get(position));
                initGridView(directories.get(position));
                //setup our image grid for the directory chosen
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private ArrayList fixDirectories(ArrayList directoryNames){
        ArrayList<String> emptyDirectories = new ArrayList<>();
        for (String s : directories) {
            if ( FileSearch.getFilePaths(s).isEmpty()) {
                emptyDirectories.add(s);
            } else {
                int index = s.lastIndexOf("/") + 1;
                String string = s.substring(index);
                directoryNames.add(string);
            }
        }
        for (int i = 0; i<emptyDirectories.size(); i++){
            directories.remove(emptyDirectories.get(i));
        }
        return directoryNames;
    }

    private void initGridView(String directoryName){
        Log.d(TAG, "Entering " + directoryName + " to retrieve files.");
        final ArrayList<String> imageURLs = FileSearch.getFilePaths(directoryName);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_OF_COLUMNS;
        userGallery.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_images, appendFilePrefix, imageURLs);
        userGallery.setAdapter(adapter);

        try {
            //set the first image to the display
            setImage(imageURLs.get(0), selectedPicture, appendFilePrefix);

            userGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemClick: selected an image: " + imageURLs.get(position));

                    setImage(imageURLs.get(position), selectedPicture, appendFilePrefix);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
