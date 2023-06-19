package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukarram.superioruniversity.R;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Courses_Model;

public class CourseAdapter extends BaseAdapter {

    String[] CourseName;
    int[] CreditHours;
    Context context;
    DatabaseReference reference;
    GoogleSignInAccount acct;



    public CourseAdapter(String[] courseName, int[] creditHours, Context context) {
        CourseName = courseName;
        CreditHours = creditHours;
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("Student");
        acct = GoogleSignIn.getLastSignedInAccount(context);

    }


    @Override
    public int getCount() {
        return CourseName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView C_name, C_hours;
        Button Enroll;


        //defining hooks
        view = LayoutInflater.from(context).inflate(R.layout.enroll_course_layout, null);
        C_name = view.findViewById(R.id.CourseName);
        C_hours = view.findViewById(R.id.crHour);
        Enroll = view.findViewById(R.id.Enroll);


        //setting text
        C_name.setText(CourseName[i]);
        C_hours.setText("Credit Hour: Cr. " + CreditHours[i] + ".0");


        String UId = acct.getId();
        reference.child(UId).child("Enrolled Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //getting total credit Hours

                int total = 0;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    int number = Integer.valueOf(ds.child("Credit_Hours").getValue(Integer.class));
                    total = total + number;
                }

                //calculating total fee
                int creditHourFee= 11000;
                int totalFee=0;
                totalFee=creditHourFee*total;

                //send Fee to database
                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Student").child(UId);
                reference2.child("Student Fee").setValue(totalFee).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });




               //validating total courses
                int CountCourses = (int) snapshot.getChildrenCount();
                if (CountCourses < 6) {

                    Enroll.setEnabled(true);

                } else {
                    Toast.makeText(context, "You can't Enroll more than 6 Courses", Toast.LENGTH_SHORT).show();
                    Enroll.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //check course exists or not
        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Student").child(UId);
        String Course_name = CourseName[i];
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Enrolled Courses")){
                    reference2.child("Enrolled Courses").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String getCourse=dataSnapshot.child("Course_Name").getValue(String.class);
                                if (Course_name.equals(getCourse)){

                                    Enroll.setEnabled(false);
                                }


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








        //on click enroll
        Enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Course_name = CourseName[i];
                int Course_Credit = CreditHours[i];





                String UserId = acct.getId();

                HashMap<String, Object> map = new HashMap<>();
                map.put("Course_Name", Course_name);
                map.put("Credit_Hours", Course_Credit);
                reference.child(UserId).child("Enrolled Courses").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Enroll.setEnabled(false);
                            Toast.makeText(context, "Course Enrolled Successfully", Toast.LENGTH_SHORT).show();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        return view;
    }


}
