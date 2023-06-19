package com.mukarram.superioruniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import Adapters.CourseAdapter;

public class SelfEnrollmentActivity extends AppCompatActivity {
    ListView listView;
    CourseAdapter courseAdapter;
    String CourseName [];
    int Credit_Hours [];
    private ImageView Close_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_enrollment);
        listView=findViewById(R.id.list_view);
        CourseName = getResources().getStringArray(R.array.Course);
        Credit_Hours = getResources().getIntArray(R.array.Credit_Hours);
        Close_activity = findViewById(R.id.close);
        courseAdapter=new CourseAdapter(CourseName,Credit_Hours,this);
        listView.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();





        //        for close activity
        Close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelfEnrollmentActivity.this,CoursesActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}