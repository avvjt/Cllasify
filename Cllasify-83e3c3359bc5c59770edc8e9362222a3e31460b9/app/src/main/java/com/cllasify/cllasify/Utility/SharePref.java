package com.cllasify.cllasify.Utility;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePref {

    Context context;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public SharePref(Context context){
        this.context =context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setDataPref(String key, String value){
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setIntDataPref(String key, int value){
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntFromPref(String key){
        return sharedPreferences.getInt(key,0);
    }

    public static String getDataFromPref(String key){
        String s= sharedPreferences.getString(key,"");
        return s;
    }

    public static String getDefaultData(String key){
        return sharedPreferences.getString(key,key);
    }

    public static void setBooleanPref(String key,Boolean value){
        editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static Boolean getBooleanFromPref(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public static Boolean getBooleanTruePref(String key){
        return sharedPreferences.getBoolean(key,true);
    }

    public static void removePrefData(String key){
        editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearAll() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


//    public static void writeListInPref(List<NewOrders> list){
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(list);
//
//        editor = sharedPreferences.edit();
//        editor.putString("LIST_KEY",jsonString);
//        editor.apply();
//    }

//    public static List<NewOrders> readListFromPref(){
//        String jsonString = sharedPreferences.getString("LIST_KEY","");
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<NewOrders>>(){}.getType();
//        List<NewOrders> list = gson.fromJson(jsonString,type);
//        return list;
//    }

    public static void clearListFromPref(){
        editor = sharedPreferences.edit();
        editor.remove("LIST_KEY");
        editor.apply();
    }



    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences("PREFS_FILE_NAME", MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }
    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences("PREFS_FILE_NAME", MODE_PRIVATE).getBoolean(permission, true);
    }

}

