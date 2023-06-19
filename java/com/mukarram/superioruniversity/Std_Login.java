package com.mukarram.superioruniversity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Std_Login extends AppCompatActivity {
    private Button Login;
    private Button SignUp;
    Button Google;

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    ImageView logo;

    Animation topAnimantion, bottomAnimation, middleAnimation, sideAnimation;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 1;

    FirebaseAuth auth;
    DatabaseReference database;
    GoogleSignInAccount acct;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_login);

        SignUp = findViewById(R.id.btn_signUp);

        Google = findViewById(R.id.google);
        textInputEmail = findViewById(R.id.text_email);
        textInputPassword = findViewById(R.id.text_password);
        Login = findViewById(R.id.SignIn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        acct = GoogleSignIn.getLastSignedInAccount(this);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConfirm();
            }
        });


        //for signup
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Std_Login.this, std_register.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });


        //for animation

        logo = findViewById(R.id.logos);

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);


        //-----------Setting Animations to the elements of Splash Screen

        logo.setAnimation(topAnimantion);
        textInputEmail.setAnimation(sideAnimation);
        textInputPassword.setAnimation(sideAnimation);
        Login.setAnimation(middleAnimation);
        Google.setAnimation(bottomAnimation);


        //for google signIn
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(Std_Login.this, gso);


//sign up with google
        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(Std_Login.this);
                progressDialog.setMessage("Signing");
                progressDialog.show();
                Intent signinIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signinIntent, RC_SIGN_IN);


            }


        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


            // Signed in successfully, show authenticated UI.
            Toast.makeText(Std_Login.this, "Signing Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            startActivity(new Intent(Std_Login.this, StudentDashboard.class));
            finish();

        }catch(ApiException e)

    {
        // The ApiException status code indicates the detailed failure reason.
        // Please refer to the GoogleSignInStatusCodes class reference for more information.
        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

    }

}



    private boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String Email = textInputEmail.getEditText().getText().toString().trim();
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


    private boolean validatePassword() {
        String Password = textInputPassword.getEditText().getText().toString().trim();
        if (Password.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError("null");
            textInputPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void InputConfirm() {
        if (!validatePassword() | !validateEmail()) {
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(acct!=null){
            startActivity(new Intent(Std_Login.this,StudentDashboard.class));
            finish();
        }
    }
}