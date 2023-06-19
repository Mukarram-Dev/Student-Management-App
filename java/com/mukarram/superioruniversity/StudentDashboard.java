package com.mukarram.superioruniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Adapters.FeeAdapter;
import Adapters.GradeAdapter;
import Adapters.TableAdapter;
import Model.Attend_Model;
import Adapters.attendanceAdapter;
import Model.Fee_Model;
import Model.Grade_Model;
import Model.Time_Table;
import Model.Time_Table_Model;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView Drawer;
    DrawerLayout dLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions gso;
    DatabaseReference database;
    GoogleSignInAccount acct;
    String Uid;

    RecyclerView recyclerAttend;
    ArrayList<Attend_Model> ModelAttend;
    attendanceAdapter attendanceAdapter;


    RecyclerView recyclerTable;
    ArrayList<Time_Table> ModelTime;
    TableAdapter tableAdapter;


    RecyclerView recyclerFee;
    ArrayList<Fee_Model> fee_models;
    FeeAdapter feeAdapter;


    RecyclerView recyclerGrade;
    ArrayList<Grade_Model> gradeModels;
    GradeAdapter gradeAdapter;

    CircleImageView profile;
    AppBarLayout appBarLayout;



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);


        database = FirebaseDatabase.getInstance().getReference();
        appBarLayout = findViewById(R.id.topbar);
        toolbar = findViewById(R.id.toolbar);
        profile = findViewById(R.id.profile);


        //recycler for Attendance
        recyclerAttend = findViewById(R.id.recycler_attendance);
        recyclerAttend.setHasFixedSize(true);
        recyclerAttend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ModelAttend = new ArrayList<>();
        attendanceAdapter = new attendanceAdapter(this);
        recyclerAttend.setAdapter(attendanceAdapter);
        attendanceAdapter.notifyDataSetChanged();

        //recycler for timetable
        recyclerTable = findViewById(R.id.time_table);
        recyclerTable.setHasFixedSize(true);
        recyclerTable.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ModelTime = new ArrayList<Time_Table>();
        tableAdapter = new TableAdapter(this,ModelTime);
        recyclerTable.setAdapter(tableAdapter);

        //getting current day
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String today = sdf.format(d);


        acct=GoogleSignIn.getLastSignedInAccount(this);
        DatabaseReference reference=database.child("Student").child(acct.getId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Time Table")) {
                    reference.child("Time Table").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ModelTime.clear();
                            for (DataSnapshot dataSnapshot : snapshot.child(today).getChildren()) {

                                    //get values from firebase
                                    String getDay=dataSnapshot.child("Day").getValue(String.class);
                                    String getSrtTime=dataSnapshot.child("StartTime").getValue(String.class);
                                    String getEndTime=dataSnapshot.child("EndTime").getValue(String.class);
                                    String getCourse=dataSnapshot.child("Course").getValue(String.class);
                                    String getTeacher=dataSnapshot.child("Teacher").getValue(String.class);
                                    String getRoom=dataSnapshot.child("Room").getValue(String.class);

                                    Time_Table model=new Time_Table(getDay,getRoom,getTeacher,getCourse,getSrtTime,getEndTime);
                                    ModelTime.add(model);

                            }
                            tableAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tableAdapter.notifyDataSetChanged();


        //recycler for fee
        recyclerFee = findViewById(R.id.recycler_fee);
        recyclerFee.setHasFixedSize(true);
        recyclerFee.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fee_models = new ArrayList<>();
        feeAdapter = new FeeAdapter(this);
        recyclerFee.setAdapter(feeAdapter);
        feeAdapter.notifyDataSetChanged();


        //recycler for cgpa
        recyclerGrade = findViewById(R.id.recycler_cgpa);
        recyclerGrade.setHasFixedSize(true);
        recyclerGrade.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gradeModels = new ArrayList<>();

        gradeAdapter = new GradeAdapter(this);
        recyclerGrade.setAdapter(gradeAdapter);
        gradeAdapter.notifyDataSetChanged();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String personPhoto = acct.getPhotoUrl().toString();


            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if (snapshot.hasChild("Student") &&dataSnapshot.hasChild(personId)){
                            Log.e("If Coundition",personName);
                            return;

                        }
                        else {
                            //sent data to database
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("personId", personId);
                            map.put("personName", personName);
                            map.put("personEmail", personEmail);
                            map.put("personPhoto", personPhoto);
                            map.put("Address", "");
                            map.put("Number", "");
                            map.put("Cnic", "");
                            map.put("Degree", "");
                            map.put("City", "");
                            map.put("Section", "");
                            map.put("Semester", "");




                            database.child("Student").child(personId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }


        //for navigation drawayer

        Drawer = findViewById(R.id.drawer);
        dLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();


        //for profile

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentDashboard.this, ActivityProfile.class));
                Toast.makeText(StudentDashboard.this, "Please Edit Profile for providing Your Information", Toast.LENGTH_LONG).show();

            }
        });




    }


    private void signout() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(StudentDashboard.this, "SignOut", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentDashboard.this, Std_Login.class));
                        finish();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //for closing drawer
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                signout();
                break;
            case R.id.admissions:
                Toast.makeText(StudentDashboard.this, "Admissions", Toast.LENGTH_SHORT).show();
                break;
            case R.id.societies:
                Toast.makeText(StudentDashboard.this, "Socities", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(StudentDashboard.this, "about", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accademics:
                Toast.makeText(StudentDashboard.this, "Accademics", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,CoursesActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(StudentDashboard.this, ActivityProfile.class));
                break;
            default:
                return false;
        }


        return true;

    }
}
