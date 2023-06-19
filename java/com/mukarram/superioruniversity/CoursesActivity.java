package com.mukarram.superioruniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CoursesActivity extends AppCompatActivity {
    Button enCourse, sCourse, tymTable,addMarks,ShowResults;
    private ImageView Close_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        enCourse = findViewById(R.id.enCourse);
        sCourse = findViewById(R.id.selfEnrol);
        tymTable = findViewById(R.id.tymTable);
        addMarks = findViewById(R.id.addMarks);
        ShowResults=findViewById(R.id.showResult);
        Close_activity = findViewById(R.id.close);


        enCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, EnrolledActtivity.class));
                finish();
            }
        });

        sCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, SelfEnrollmentActivity.class));
                finish();
            }
        });


        tymTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, TimeTable.class));
                finish();
            }
        });





        ShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, ResultActivity.class));
                finish();
            }
        });


        addMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, AddMarks.class));
                finish();
            }
        });


        //        for close activity
        Close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoursesActivity.this, StudentDashboard.class);
                startActivity(intent);
                finish();

            }
        });
    }
}