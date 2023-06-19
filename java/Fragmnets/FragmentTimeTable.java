package Fragmnets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukarram.superioruniversity.CoursesActivity;
import com.mukarram.superioruniversity.R;
import com.mukarram.superioruniversity.StudentDashboard;

import java.util.ArrayList;

import Adapters.TableAdapter;
import Adapters.TymTableAdapter;
import Model.Time_Table_Model;


public class FragmentTimeTable extends Fragment {

    RecyclerView recyclerTable;
    ArrayList<Time_Table_Model> tableList;
    TymTableAdapter tymTableAdapter;
    DatabaseReference reference;
    GoogleSignInAccount acct;
    String Uid;


    public FragmentTimeTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        acct = GoogleSignIn.getLastSignedInAccount(getContext());
        Uid = acct.getId();
        reference = FirebaseDatabase.getInstance().getReference("Student").child(Uid);


        //recycler for timetable
        recyclerTable = view.findViewById(R.id.recycler_time);
        recyclerTable.setHasFixedSize(true);
        recyclerTable.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        tableList = new ArrayList<>();
        tymTableAdapter = new TymTableAdapter(getContext(), tableList);
        recyclerTable.setAdapter(tymTableAdapter);



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Time Table")) {
                    reference.child("Time Table").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tableList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    //get values from firebase
                                    String getDay=dataSnapshot1.child("Day").getValue(String.class);
                                    String getSrtTime=dataSnapshot1.child("StartTime").getValue(String.class);
                                    String getEndTime=dataSnapshot1.child("EndTime").getValue(String.class);
                                    String getCourse=dataSnapshot1.child("Course").getValue(String.class);
                                    String getTeacher=dataSnapshot1.child("Teacher").getValue(String.class);
                                    String getRoom=dataSnapshot1.child("Room").getValue(String.class);

                                    Time_Table_Model model=new Time_Table_Model(getDay,getRoom,getTeacher,getCourse,getSrtTime,getEndTime);
                                    Log.e("Valuess===", model.getSubject());
                                    tableList.add(model);

                                }
                                tymTableAdapter.notifyDataSetChanged();

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




        return view;
    }
}