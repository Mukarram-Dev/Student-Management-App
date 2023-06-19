package com.mukarram.superioruniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddMarks extends AppCompatActivity {
    EditText marks;
    Button add, update;
    DatabaseReference reference;
    GoogleSignInAccount acct;
    ProgressDialog progressDialog;
    String Uid,course,getMarks,grade;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marks);
        marks = findViewById(R.id.subMarks);
        add = findViewById(R.id.addMarks);
        update = findViewById(R.id.UpdateMarks);
        progressDialog = new ProgressDialog(this);
        close=findViewById(R.id.close);










        //for set courses in spinner
        acct = GoogleSignIn.getLastSignedInAccount(this);
        Uid = acct.getId();
        reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid).child("Enrolled Courses");
        Spinner spCourse = (Spinner) findViewById(R.id.spin_courses);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> courses = new ArrayList<String>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String courseNames = dataSnapshot.child("Course_Name").getValue(String.class);
                    courses.add(courseNames);
                }

                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AddMarks.this, android.R.layout.simple_spinner_item, courses);

                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spCourse.setAdapter(courseAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //save data in firebase
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding Marks");
                progressDialog.setTitle("Please Wait");
                progressDialog.setCancelable(true);
                progressDialog.show();


                if (!(marks.getText().toString().equals(""))){
                    getMarks=marks.getText().toString();
                    String getGrade=calGrade(getMarks);
                    double getGpa=calGpa(getGrade);
                    //checking marks already added

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Student").child(Uid);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("Assessments")){
                                reference1.child("Assessments").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(course)){
                                            Toast.makeText(AddMarks.this, "You have Already Added this course!Now You can Update Marks ", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            marks.setText("");
                                            return;
                                        }
                                        else {
                                            addMarks(course,getMarks,getGrade,getGpa);
                                        }
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
                }
                else {
                    Toast.makeText(AddMarks.this, "Marks Can't be null", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }







            }
        });









        //update marks
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getMarks;
                if (!(marks.getText().toString().equals(""))) {
                    getMarks = marks.getText().toString();
                    String getGrade = calGrade(getMarks);
                    double getGpa = calGpa(getGrade);

                    updataMarks(course,getMarks,getGrade,getGpa);
                }
                else {
                    Toast.makeText(AddMarks.this, "Marks Can't be null", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
            }
        });





        //        for close activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMarks.this, CoursesActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void updataMarks(String course, String getMarks, String getGrade, double getGpa) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("Course", course);
        map.put("Marks", getMarks);
        map.put("Grade", getGrade);
        map.put("Gpa", getGpa);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1 = reference.child("Student").child(Uid);

        reference1.child("Assessments").child(course).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddMarks.this, "Update Marks Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calGpa(String getGrade) {
        double Gpa;

        switch (getGrade) {
            case "A+":
                Gpa = 4.0;
                break;
            case "A":
                Gpa = 3.66;
                break;
            case "B":
                Gpa = 3.0;
                break;
            case "C":
                Gpa = 2.0;
                break;
            case "D":
                Gpa = 1.0;
                break;
            default:
                Gpa = 0.0;
                break;
        }
       return Gpa;
    }

    private String calGrade(String getMarks) {
        String grade;


       if (Integer.parseInt(getMarks)>=50 &&Integer.parseInt(getMarks)<60){
            grade="D";
        }
        else if (Integer.parseInt(getMarks)>=60 &&Integer.parseInt(getMarks)<70){
            grade="C";
        }
        else if (Integer.parseInt(getMarks)>=70 &&Integer.parseInt(getMarks)<80){
            grade="B";
        }
        else if (Integer.parseInt(getMarks)>=80 &&Integer.parseInt(getMarks)<90){
            grade="A";
        }
       else if (Integer.parseInt(getMarks)>=90 &&Integer.parseInt(getMarks)<=100){
           grade="A+";
       }
        else {
            grade="F";

       }
        return grade;

    }


    private void addMarks(String course, String getMarks, String getGrade, double getGpa) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("Course", course);
        map.put("Marks", getMarks);
        map.put("Grade", getGrade);
        map.put("Gpa", getGpa);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1 = reference.child("Student").child(Uid);

        reference1.child("Assessments").child(course).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(AddMarks.this, "Marks Added Successfully", Toast.LENGTH_SHORT).show();
                    marks.setText("");
                }
                else {
                    Toast.makeText(AddMarks.this, "Marks Can't be added", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddMarks.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }
}