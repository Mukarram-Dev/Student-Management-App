package Fragmnets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import com.mukarram.superioruniversity.ActivityProfile;
import com.mukarram.superioruniversity.EditProfile;
import com.mukarram.superioruniversity.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Model.Courses_Model;


public class AddTimeTable extends Fragment {
    EditText strTime, endTime;
    Button add, update;
    DatabaseReference reference;
    GoogleSignInAccount acct;
    ProgressDialog progressDialog;
    String Uid;
    String format,format2;
    private String room, teacher, course, day, startTime, EndTime;

    public AddTimeTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_time_table, container, false);


        strTime = view.findViewById(R.id.strtTime);
        endTime = view.findViewById(R.id.endTime);
        add = view.findViewById(R.id.addTable);
        update = view.findViewById(R.id.UpdateTable);
        progressDialog = new ProgressDialog(getContext());

        // perform click event listener on edit text
        //getting start time
        strTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0) {

                            selectedHour += 12;

                            format = "AM";
                        }
                        else if (selectedHour == 12) {

                            format = "PM";

                        }
                        else if (selectedHour > 12) {

                            selectedHour -= 12;

                            format = "PM";

                        }
                        else {

                            format = "AM";
                        }




                        strTime.setText(selectedHour + ":" + selectedMinute+" "+format);
                        startTime = strTime.getText().toString();
                        Log.e("Starting Time===", startTime);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();

            }
        });

        // perform click event listener on edit text
        //getting end time
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0) {

                            selectedHour += 12;

                            format2 = "AM";
                        }
                        else if (selectedHour == 12) {

                            format2 = "PM";

                        }
                        else if (selectedHour > 12) {

                            selectedHour -= 12;

                            format2 = "PM";

                        }
                        else {

                            format2 = "AM";
                        }



                        endTime.setText(selectedHour + ":" + selectedMinute+" "+format2);
                        EndTime = endTime.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        //for set days in spinner
        Spinner spdays = (Spinner) view.findViewById(R.id.spin_days);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter
                .createFromResource(getContext(), R.array.Days,
                        android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdays.setAdapter(Adapter);

        spdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day = adapterView.getItemAtPosition(i).toString();
                Log.e("Day===", day);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for set teachers in spinner
        Spinner spdteacher = (Spinner) view.findViewById(R.id.spin_teacher);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.teachers,
                        android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdteacher.setAdapter(arrayAdapter);


        spdteacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teacher = adapterView.getItemAtPosition(i).toString();
                Log.e("TEacher===", teacher);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for set room in spinner
        Spinner spdRoom = (Spinner) view.findViewById(R.id.spin_rooms);
        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter
                .createFromResource(getContext(), R.array.Rooms,
                        android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdRoom.setAdapter(arrayAdapter2);


        spdRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                room = adapterView.getItemAtPosition(i).toString();
                Log.e("Room===", room);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //for set courses in spinner
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
        Uid = acct.getId();
        reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid).child("Enrolled Courses");
        Spinner spCourse = (Spinner) view.findViewById(R.id.spin_courses);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> courses = new ArrayList<String>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String courseNames = dataSnapshot.child("Course_Name").getValue(String.class);
                    courses.add(courseNames);
                }

                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courses);

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
                progressDialog.setMessage("Updating Profile");
                progressDialog.setTitle("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                addTable(room, teacher, day, course, startTime, EndTime);
            }
        });


        //update data in firebase


        return view;
    }


    private void addTable(String Room, String Teacher, String Day, String Course, String StartTime, String EndTime) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("Room", Room);
        map.put("Teacher", Teacher);
        map.put("Course", Course);
        map.put("StartTime", StartTime);
        map.put("EndTime", EndTime);
        map.put("Day", Day);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1 = reference.child("Student").child(Uid);

        reference1.child("Time Table").child(Day).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Time Table Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}