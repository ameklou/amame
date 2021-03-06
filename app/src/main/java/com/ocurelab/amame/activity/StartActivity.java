package com.ocurelab.amame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ocurelab.amame.R;
import com.ocurelab.amame.utils.Preferences;
import com.ocurelab.amame.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {
    private MaterialButton loginBtn , registrationBtn;
    private View loginLayout, loadingLayout;
    private TextInputEditText phoneNumber;
    private TextInputLayout phoneLayout;
    private ProgressDialog progressDialog;
    private Preferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private void startPhoneNumberVerification(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumber, 20L, TimeUnit.SECONDS, this, mCallbacks);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        FirebaseApp.initializeApp(this);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_start);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="https://api.amame.org/api/login/";



        if(preferences.isWaitingForCode()) {
            startActivity(new Intent(this, OtpActivity.class));
            finish();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vérification du numéro de téléphone ...");
        progressDialog.setProgressStyle(0);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                mAuth.addAuthStateListener(mAuthListener);
            }
        }, 2000L);

        loadingLayout=findViewById(R.id.loading_block);
        loginLayout=findViewById(R.id.login_block);
        loginBtn=findViewById(R.id.login_btn);
        phoneLayout=findViewById(R.id.phoneLayout);
        phoneNumber=findViewById(R.id.phoneNumber);
        registrationBtn=findViewById(R.id.registration_btn);

        loginBtn.setOnClickListener(v->{
            StringRequest loginRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String token= jsonObject.getString("token");
                        preferences.setToken(token);
                        JSONObject user = new JSONObject(jsonObject.getString("user"));
                        String username = user.getString("username");
                        String id = user.getString("id");
                        preferences.setUserId(id);
                        Log.d("loginRes",username);
                        preferences.setUsername(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onValidateStep();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params=new HashMap<String,String>();
                    params.put("phone",phoneNumber.getText().toString().trim());
                    params.put("password","open@bunshin");
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }

            };
            queue.add(loginRequest);

        });

        registrationBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,RegistrationActivity.class));
            finish();
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                preferences.setVerificationId(verificationId);
                preferences.setIsWaitingForCode(true);
                Log.d(StartActivity.class.getSimpleName(), "onCodeSent:" + verificationId);
                mResendToken = token;
                preferences.setResendToken(token.toString());
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                startActivity(new Intent(StartActivity.this, OtpActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException exception) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Log.w(StartActivity.class.getSimpleName(), exception);
                if(exception instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(StartActivity.this, "Numéro de téléphone invalide !", Toast.LENGTH_LONG).show();
                } else if(exception instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(StartActivity.this, "Une erreur est survenue, reessayez plus tard !", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        };


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                if(auth.getCurrentUser() == null) {
                    loadingLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                } else {
                    loadingLayout.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        };



    }

    private void onValidateStep() {
        if(!Util.isConnected(this)) {
            Toast.makeText(this, "Vous n'êtes pas connecté à internet !", Toast.LENGTH_SHORT).show();
        } else if(Util.isValidPhoneNumber(phoneNumber.getText().toString())) {
            final String mobile = "+228" + phoneNumber.getText().toString();
            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    preferences.setMobileNumber(mobile);
                    startPhoneNumberVerification(mobile);

                }
            }, 2000L);
        } else {
            Toast.makeText(this, "Veuillez saisir un numéro de téléphone valide !", Toast.LENGTH_LONG).show();
            phoneNumber.requestFocus();
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        if(mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    protected void onStop() {
        super.onStop();
        if(mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

}
