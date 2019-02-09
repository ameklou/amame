package com.ocurelab.amame.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;

public class Util {
    public static final String OTP_ORIGIN = "Phone Code";
    public static String OTP_EXTRA = "otp_extra";
    public static String OTP_BROADCAST = "com.ocurelab.amame.intent.action.CODE_RECEIVED";


    public static  void gotoActivity(Context context, Class<?> c){
        Intent i = new Intent(context,c);
        context.startActivity(i);

    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean isValidPhoneNumber(String mobile) {
        return mobile.matches("^[0-9]{8}$");
    }


    @Nullable
    public static FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser() ; }

    public Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }
}
