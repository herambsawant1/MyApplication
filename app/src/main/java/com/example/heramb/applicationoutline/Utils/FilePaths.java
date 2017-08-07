package com.example.heramb.applicationoutline.Utils;

import android.os.Environment;

/**
 * Created by heram on 8/6/2017.
 */

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

}