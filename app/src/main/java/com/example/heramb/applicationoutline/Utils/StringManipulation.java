package com.example.heramb.applicationoutline.Utils;

/**
 * Created by heram on 7/22/2017.
 */

public class StringManipulation {

    public static String expandUsername(String username){
        return username.replace(".", " ");
    }

    public static String condenseUsername(String username){
        return username.replace(" " , ".");
    }
}
