package com.ocurelab.amame.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ocurelab.amame.utils.Util;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    if (!senderAddress.toLowerCase().equals(Util.OTP_ORIGIN.toLowerCase())){
                        return;
                    }
                    Intent i = new Intent(Util.OTP_BROADCAST);
                    i.putExtra(Util.OTP_EXTRA,getVerificationCode(message));
                    context.sendBroadcast(i);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private String getVerificationCode(String message) {
        return message.substring(0, 6);
    }
}
