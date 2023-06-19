package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukarram.superioruniversity.FeeInvoice;
import com.mukarram.superioruniversity.R;

public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.ViewHolder> {
    Context mcontext;
    DatabaseReference reference;
    GoogleSignInAccount acct;


    public FeeAdapter(Context mcontext) {
        this.mcontext = mcontext;
        acct = GoogleSignIn.getLastSignedInAccount(mcontext);
        reference= FirebaseDatabase.getInstance().getReference("Student").child(acct.getId());
    }

    @NonNull
    @Override
    public FeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.fee_layout, null);
        return new FeeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeeAdapter.ViewHolder holder, int position) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Student Fee")){
                    int totalFee=snapshot.child("Student Fee").getValue(Integer.class);
                    holder.totalFee.setText(String.valueOf(totalFee));
                }
                else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcontext.startActivity(new Intent(mcontext,FeeInvoice.class));
            }
        });



    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalFee;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalFee=itemView.findViewById(R.id.rupees);
        }
    }
}
