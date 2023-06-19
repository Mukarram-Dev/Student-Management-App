package com.mukarram.superioruniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    private ImageView Close_activity;
    private ImageView profile_pic;
    private TextView save_profile,chng_photo;
    private EditText fname,address,no,cnic;
    private String department,city,semester,section;

    ProgressDialog progressDialog;
    DatabaseReference reference;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions gso;
    GoogleSignInAccount acct;
    String userID;


    private final int REQ = 1;
    private Bitmap bitmap = null;
    String downloadUrl,ImageUrl;

    private com.google.firebase.storage.StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile_pic = findViewById(R.id.image_profile);
        save_profile = findViewById(R.id.save);
        chng_photo = findViewById(R.id.photo_change);
        fname = findViewById(R.id.Full_name);
        address = findViewById(R.id.address);
        no = findViewById(R.id.number);
        cnic=findViewById(R.id.cnic);

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog=new ProgressDialog(EditProfile.this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        userID=acct.getId();





        Close_activity=findViewById(R.id.close);
        //for close activity
        Close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this,ActivityProfile.class));
                finish();
            }
        });

        //for set cities in spinner
        Spinner spCity = (Spinner) findViewById(R.id.spin_city);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter
                .createFromResource(this, R.array.City_Array,
                        android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(Adapter);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for set departments in spinner
        Spinner spdeprt = (Spinner) findViewById(R.id.spin_department);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.Array_Department,
                        android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdeprt.setAdapter(arrayAdapter);


        spdeprt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               department= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //for set section in spinner
        Spinner spSection = (Spinner) findViewById(R.id.spin_section);
        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter
                .createFromResource(this, R.array.Section,
                        android.R.layout.simple_spinner_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSection.setAdapter(arrayAdapter3);


        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                section= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for set semester in spinner
        Spinner spSemester = (Spinner) findViewById(R.id.spin_semester);
        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter
                .createFromResource(this, R.array.Semesters,
                        android.R.layout.simple_spinner_item);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSemester.setAdapter(arrayAdapter4);


        spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester= adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for changing photo
        chng_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickimg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickimg,REQ);

            }
        });






        //setting text on edit text from database
        reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1=reference.child("Student").child(userID);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {






                String Address=snapshot.child("Address").getValue(String.class);
                String City=snapshot.child("City").getValue(String.class);
                String Degree=snapshot.child("Degree").getValue(String.class);
                String Cnic=snapshot.child("Cnic").getValue(String.class);
                String Number=snapshot.child("Number").getValue(String.class);
                String Name=snapshot.child("personName").getValue(String.class);
                ImageUrl =snapshot.child("personPhoto").getValue(String.class);




                fname.setText(Name);


                Glide.with(EditProfile.this).load(ImageUrl).into(profile_pic);


                if (!Cnic.equals("")){

                    cnic.setText(Cnic);
                }




                if(!Number.equals("")){

                    no.setText(Number);
                }
                if(!Address.equals("")){

                    address.setText(Address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //save data in firebase
        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Profile");
                progressDialog.setTitle("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                updateProfile(fname.getText().toString(),
                        cnic.getText().toString(),
                        address.getText().toString(),
                        no.getText().toString(),
                        city,
                        department,section,semester,ImageUrl);
            }
        });
    }



    private void updateProfile(String fullname, String cnic, String address, String number, String city, String department, String section, String semester, String imageUrl) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1=reference.child("Student").child(userID);
reference1.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("personName", fullname);
        map.put("Address", address);
        map.put("Number", number);
        map.put("Cnic", cnic);
        map.put("Degree", department);
        map.put("City", city);
        map.put("Section", section);
        map.put("Semester", semester);
        map.put("personPhoto", downloadUrl);


        reference1.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                startActivity(new Intent(EditProfile.this,ActivityProfile.class));
                 finish();
                Toast.makeText(EditProfile.this, "Profile Update", Toast.LENGTH_SHORT).show();



            }
        });

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


    }




    protected void onActivityResult(int requestCode, int resultCode,  Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQ && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                uploadImage();


            }catch (IOException e){
                e.printStackTrace();
            }
            profile_pic.setImageBitmap(bitmap);
        }

    }


    private void uploadImage() {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 50 , boas);
        byte[] finalimg = boas.toByteArray();
        final com.google.firebase.storage.StorageReference filepath;
        filepath = storageReference.child("Profile").child(finalimg+ "jpg");
        final UploadTask uploadtask = filepath.putBytes(finalimg);
        uploadtask.addOnCompleteListener(EditProfile.this, task -> {
            if(task.isSuccessful())
            {
                uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = String.valueOf(uri);


                            }
                        });
                    }
                });

            }else
            {
                Toast.makeText(EditProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }








    }

