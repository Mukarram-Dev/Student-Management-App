package com.mukarram.superioruniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.CourseAdapter;
import Adapters.GetCoursesAdapter;
import Model.Courses_Model;

public class EnrolledActtivity extends AppCompatActivity {
    ListView listView;
    GetCoursesAdapter getCoursesAdapter;
    ArrayList<Courses_Model> cList;
    DatabaseReference reference;
    GoogleSignInAccount acct;
    private ImageView Close_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_acttivity);
        listView = findViewById(R.id.list_View);
        Close_activity = findViewById(R.id.close);
        cList = new ArrayList<>();
        getCoursesAdapter = new GetCoursesAdapter(cList, EnrolledActtivity.this);
        listView.setAdapter(getCoursesAdapter);


        acct = GoogleSignIn.getLastSignedInAccount(this);
        String Uid = acct.getId();


        //getting data from firebase
        reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid).child("Enrolled Courses");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Courses_Model model1 = new Courses_Model();

                    final String CourseName = dataSnapshot.child("Course_Name").getValue(String.class);
                    final int CreditHours = dataSnapshot.child("Credit_Hours").getValue(Integer.class);

                    Courses_Model model = new Courses_Model(CourseName, CreditHours);
                    cList.add(model);
                    getCoursesAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //        for close activity
        Close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EnrolledActtivity.this,CoursesActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}