package Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukarram.superioruniversity.R;
import com.mukarram.superioruniversity.ResultActivity;
import com.mukarram.superioruniversity.TimeTable;

import Model.Model_Result;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {

    DatabaseReference reference;
    GoogleSignInAccount acct;
    Context mContext;

    public GradeAdapter(Context mContext) {
        this.mContext = mContext;
        acct = GoogleSignIn.getLastSignedInAccount(mContext);
        reference = FirebaseDatabase.getInstance().getReference("Student").child(acct.getId());
    }

    @NonNull
    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gpa_layout, null);
        return new GradeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeAdapter.ViewHolder holder, int position) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Assessments")) {
                    reference.child("Assessments").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double total = 0.0;
                            int countCourse;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                //get values from firebase
                                countCourse = (int) snapshot.getChildrenCount();
                                Double getGpa = dataSnapshot.child("Gpa").getValue(Double.class);
                                total = total + getGpa;
                                double GetCgpa = total / countCourse;
                                holder.cGpa.setText(String.valueOf(GetCgpa));


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(mContext, ResultActivity.class);
                       mContext.startActivity(intent);
                   }
               });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cGpa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cGpa = itemView.findViewById(R.id.cgpa);
        }
    }
}
