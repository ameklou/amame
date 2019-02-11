package com.ocurelab.amame.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences preferences;
    private static final String CREDENTIAL = "credential_";
    private static final String IS_WAITING_FOR_SMS = "waiting";
    private static final String MOBILE_NUMBER = "phone_number";
    private static final String PREF_NAME = "com.ocurelab.amame";
    private static final String RESEND_TOKEN = "resend_token";
    private static final String VERIFICATION_ID = "verification_id";
    private static final String USERNAME="username";
    private static final String TOKEN = "token";
    private static final String USER_ID = "user_id";


    public Preferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }


    public String getCredential() {
        return preferences.getString(CREDENTIAL,null);
    }
    public String getUserId() {
        return preferences.getString(USER_ID,null);
    }

    public String getMobileNumber() {
        return preferences.getString(MOBILE_NUMBER, null);
    }

    public String getVerificationId() {
        return preferences.getString(VERIFICATION_ID, null);
    }

    public boolean isWaitingForCode() {
        return preferences.getBoolean(IS_WAITING_FOR_SMS, false);
    }

    public void setCredential(String credential) {
        preferences.edit().putString(CREDENTIAL, credential).commit();
    }

    public void setIsWaitingForCode(boolean waitingForSms) {
        preferences.edit().putBoolean(IS_WAITING_FOR_SMS, waitingForSms).commit();
    }

    public void setMobileNumber(String mobileNumber) {
        preferences.edit().putString(MOBILE_NUMBER, mobileNumber).commit();
    }

    public void setResendToken(String token) {
        preferences.edit().putString(RESEND_TOKEN, token).commit();
    }

    public void setVerificationId(String verificationId) {
        preferences.edit().putString(VERIFICATION_ID, verificationId).commit();
    }

    public String getUsername(){
        return preferences.getString(USERNAME,null);
    }

    public void setUserId(String user_id){
         preferences.edit().putString(USER_ID,user_id).commit();
    }
    public void setUsername(String username){
        preferences.edit().putString(USERNAME,username).commit();
    }


    public String getToken(){
        return preferences.getString(TOKEN,null);
    }

    public void setToken(String token){
        preferences.edit().putString(TOKEN,token).commit();
    }
}
