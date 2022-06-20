package com.project3.storemanagement.utils;

public class InputStringModifier {
    public static String capitalizeFirstWord(String str){
        String capitalizeFirstWord=str.substring(0, 1).toUpperCase() + str.substring(1);
        return capitalizeFirstWord;
    }
}
