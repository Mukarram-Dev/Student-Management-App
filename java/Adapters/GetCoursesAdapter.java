package Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukarram.superioruniversity.AddMarks;
import com.mukarram.superioruniversity.R;

import java.util.ArrayList;

import Model.Courses_Model;

public class GetCoursesAdapter extends ArrayAdapter<Courses_Model> {
    Context context;
    ArrayList<Courses_Model> cList;
    DatabaseReference reference;
    GoogleSignInAccount acct;

    public GetCoursesAdapter(ArrayList<Courses_Model> cList, Context context) {
        super(context, 0, cList);
        this.context = context;
        this.cList = cList;

    }

    @Override
    public int getCount() {
        return cList.size();
    }


    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView C_name, C_hours;
        Button drop;


        //defining hooks
        view = LayoutInflater.from(context).inflate(R.layout.course_enrolled_layout, null);
        C_name = view.findViewById(R.id.CourseName);
        C_hours = view.findViewById(R.id.crHour);
        drop = view.findViewById(R.id.Drop);


        //setting text
        Courses_Model model = cList.get(i);

        String CourseName = model.getCourse_name();
        int CreditHours = model.getCredit_Hours();


        C_name.setText(model.getCourse_name());
        C_hours.setText("Credit Hour: Cr. " + model.getCredit_Hours() + ".0");


        //get total Credit Hours
        acct = GoogleSignIn.getLastSignedInAccount(context);
        String Uid = acct.getId();
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Student").child(Uid).child("Enrolled Courses");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Student").child(Uid);
                reference2.child("Student Fee").setValue(totalFee).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //drop Course
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("Drop Course")
                        .setMessage("Are you sure to Drop Course")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int e) {

                                acct = GoogleSignIn.getLastSignedInAccount(context);
                                String Uid = acct.getId();

                                Courses_Model model2 = cList.get(i);
                                String CourseName = model2.getCourse_name();
                                int CreditHours = model2.getCredit_Hours();


                                reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid).child("Enrolled Courses");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String key = dataSnapshot.getKey();
                                            if (dataSnapshot.child("Course_Name").getValue().equals(CourseName)) {
                                                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Course Dropped", Toast.LENGTH_SHORT).show();
                                                        dropAssessment(CourseName);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                            private void dropAssessment(String courseName) {
                                DatabaseReference reference2=  FirebaseDatabase.getInstance().getReference("Student").child(Uid);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("Assessments")){
                                            reference2.child("Assessments").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild(courseName)){

                                                        reference2.child("Assessments").child(courseName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

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
                        }).setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog a = dialogBuilder.create();

                a.show();



            }
        });


        return view;


    }


}
