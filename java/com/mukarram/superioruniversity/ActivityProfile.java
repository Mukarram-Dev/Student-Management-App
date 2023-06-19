package com.mukarram.superioruniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions gso;
    GoogleSignInAccount acct;
    String userID;

    private CircleImageView profile_photo;
    private Button edt_profile;


    private TextView txt_username;
    private TextView txt_cnic;
    private TextView txt_city;
    private TextView txt_education;
    private TextView txt_email;
    private TextView txt_adress;
    private TextView txt_number;
    private TextView txt_section;
    private TextView txt_semester;
    private ImageView Close_activity;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        txt_username=findViewById(R.id.txt_nameUser);
        txt_city=findViewById(R.id.From);
        txt_cnic=findViewById(R.id.cnic);
        txt_email=findViewById(R.id.text_email);

        txt_education=findViewById(R.id.Education);
        txt_cnic=findViewById(R.id.cnic);
        txt_adress=findViewById(R.id.Address);
        txt_number=findViewById(R.id.number);
        txt_section=findViewById(R.id.section);
        txt_semester=findViewById(R.id.semester);

        profile_photo=findViewById(R.id.profile_Photo);
        Close_activity = findViewById(R.id.close);
        edt_profile=findViewById(R.id.edit_profile);




        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        userID=acct.getId();


        //for edit profile
        edt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityProfile.this,EditProfile.class));

            }
        });

//        for close profile
        Close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityProfile.this,StudentDashboard.class);
                startActivity(intent);
                finish();

            }
       });

    }





    //when profile activity start
    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1=reference.child("Student").child(userID);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String personPhoto =snapshot.child("personPhoto").getValue(String.class);
                String Address=snapshot.child("Address").getValue(String.class);
                String City=snapshot.child("City").getValue(String.class);
                String Degree=snapshot.child("Degree").getValue(String.class);
                String Cnic=snapshot.child("Cnic").getValue(String.class);
                String Number=snapshot.child("Number").getValue(String.class);
                String Email=snapshot.child("personEmail").getValue(String.class);
                String Name=snapshot.child("personName").getValue(String.class);
                String Semester=snapshot.child("Semester").getValue(String.class);
                String Section=snapshot.child("Section").getValue(String.class);




                txt_username.setText(Name);

                if (snapshot.hasChild("personPhoto")){
                    Picasso.get().load(personPhoto).into(profile_photo);
                }


                if (!Cnic.equals("")){
                    txt_cnic.setVisibility(View.VISIBLE);
                    txt_cnic.setText(Cnic);

                }

                if (!Email.equals("")){
                    txt_email.setVisibility(View.VISIBLE);
                    txt_email.setText(Email);

                }


                if(!City.equals("")){
                    txt_city.setVisibility(View.VISIBLE);
                    txt_city.setText("From "+City);

                }
                if(!Degree.equals("")){
                    txt_education.setVisibility(View.VISIBLE);
                    txt_education.setText("Student of "+Degree);
                }
                if(!Number.equals("")){
                    txt_number.setVisibility(View.VISIBLE);
                    txt_number.setText(Number);
                }
                if(!Address.equals("")){
                    txt_adress.setVisibility(View.VISIBLE);
                    txt_adress.setText(Address);
                }
                if(!Section.equals("")){
                    txt_section.setVisibility(View.VISIBLE);
                    txt_section.setText(Section);
                }
                if(!Semester.equals("")){
                    txt_semester.setVisibility(View.VISIBLE);
                    txt_semester.setText("Semester: "+Semester);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}