package com.mukarram.superioruniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.AdapterResult;
import Adapters.TymTableAdapter;
import Model.Model_Result;
import Model.Time_Table_Model;

public class ResultActivity extends AppCompatActivity {
    DatabaseReference reference;
    GoogleSignInAccount acct;
    String Uid;
    RecyclerView recyclerResult;
    ArrayList<Model_Result> resultList;
    AdapterResult adapterResult;
    ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        Uid = acct.getId();
        close=findViewById(R.id.close);
        reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid);
        //recycler for getting result
        recyclerResult = findViewById(R.id.recycler_result);
        recyclerResult.setHasFixedSize(true);
        recyclerResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultList = new ArrayList<>();
        adapterResult = new AdapterResult(this, resultList);
        recyclerResult.setAdapter(adapterResult);




        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Assessments")) {
                    reference.child("Assessments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            resultList.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    //get values from firebase
                                    String getCourse=dataSnapshot.child("Course").getValue(String.class);
                                    Double getGpa=dataSnapshot.child("Gpa").getValue(Double.class);
                                    String getGrade=dataSnapshot.child("Grade").getValue(String.class);
                                    String getMarks=dataSnapshot.child("Marks").getValue(String.class);



                                    Model_Result model=new Model_Result(getMarks,getGrade,getCourse,getGpa);

                                    resultList.add(model);


                            }
                            adapterResult.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        //        for close activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, CoursesActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}