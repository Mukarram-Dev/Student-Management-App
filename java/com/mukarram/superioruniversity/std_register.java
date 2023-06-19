package com.mukarram.superioruniversity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

public class std_register extends AppCompatActivity {
    private Button btn_SignIn, btn_signup, google, facebook;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private static final int REQ_ONE_TAP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btn_SignIn = findViewById(R.id.btn_login);
        textInputEmail = findViewById(R.id.text_email);
        textInputPassword = findViewById(R.id.text_password);
        textInputUsername = findViewById(R.id.text_name);
        btn_signup = findViewById(R.id.SignUp);
        google = findViewById(R.id.Google);


        //for signup with google
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(std_register.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                            null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(std_register.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });

            }
        });



//for signUP

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
            }
        });


//for go back for sign in
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(std_register.this, Std_Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
    }


    private boolean validateEmail() {
        String Email = textInputEmail.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (Email.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;

        } else if (!Email.matches(emailPattern)) {
            textInputEmail.setError("Invalid Email");
            return false;

        } else {
            textInputEmail.setError("null");
            textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName() {
        String Username = textInputUsername.getEditText().getText().toString().trim();
        if (Username.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else {
            textInputUsername.setError("null");
            textInputUsername.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String Password = textInputPassword.getEditText().getText().toString().trim();
        if (Password.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (Password.length() < 8) {
            textInputPassword.setError("Password Should be minimum 8");
            return false;
        } else {
            textInputPassword.setError("null");
            textInputPassword.setErrorEnabled(false);
            return true;
        }
    }


    public void confirmInput() {

        if (!validatePassword() | !validateEmail() | validateName()) {
            return;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d(TAG, "Got ID token.");
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }
}