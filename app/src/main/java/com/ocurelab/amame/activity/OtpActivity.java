package com.ocurelab.amame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ocurelab.amame.R;
import com.ocurelab.amame.utils.Preferences;
import com.ocurelab.amame.utils.Util;

public class OtpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Preferences preferences;
    private ProgressBar otpProgressBar;
    private View otpBoutonView;
    private MaterialButton validate,retour;
    private TextInputEditText phoneNumber, codeEditText;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Util.OTP_BROADCAST)) {
                codeEditText.setText(intent.getStringExtra(Util.OTP_EXTRA));
            }
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                otpProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    Log.d("Login", "signInWithCredential:success");
                    preferences.setIsWaitingForCode(false);
                    startActivity(new Intent(OtpActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    otpBoutonView.setVisibility(View.VISIBLE);
                    Log.w("Login", "signInWithCredential:failure", task.getException());
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(OtpActivity.this, "Code invalide!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        otpProgressBar.setVisibility(View.VISIBLE);
        otpBoutonView.setVisibility(View.GONE);
        final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        preferences.setCredential(credential.toString());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                signInWithPhoneAuthCredential(credential);
            }
        }, 2000L);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();

        otpProgressBar = findViewById(R.id.otp_progress);
        otpBoutonView = findViewById(R.id.otp_bouton_block);
        phoneNumber = findViewById(R.id.phoneNumber);
        codeEditText = findViewById(R.id.edt_code);
        validate = findViewById(R.id.bt_valider);
        retour = findViewById(R.id.bt_retour);

        phoneNumber.setText(preferences.getMobileNumber());

        validate.setOnClickListener(v->{
            if(codeEditText.getText().toString().trim().length() == 6) {
                verifyPhoneNumberWithCode(preferences.getVerificationId(), codeEditText.getText().toString());
            }else {
                Toast.makeText(OtpActivity.this, "Code invalide!", Toast.LENGTH_LONG).show();
            }
        });

        retour.setOnClickListener(v->{
            preferences.setIsWaitingForCode(false);
            startActivity(new Intent(OtpActivity.this, StartActivity.class));
            finish();
        });

    }


    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Util.OTP_BROADCAST));
    }

    public void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(Util.OTP_BROADCAST));
    }
}
