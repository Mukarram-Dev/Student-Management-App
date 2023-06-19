package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mukarram.superioruniversity.R;
import com.mukarram.superioruniversity.TimeTable;

import java.util.ArrayList;

import Model.Model_Result;
import Model.Time_Table;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.ViewHolder> {
    Context mContext;
    ArrayList<Model_Result> listResult;
    public AdapterResult(Context mContext, ArrayList<Model_Result> listResult) {
        this.mContext = mContext;
        this.listResult = listResult;
    }

    @NonNull
    @Override
    public AdapterResult.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.result_layout, null);
        return new AdapterResult.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResult.ViewHolder holder, int position) {
        Model_Result model=listResult.get(position);

        holder.Marks.setText("Marks: "+model.getMarks());
        holder.Grade.setText("Grade: "+model.getGrade());
        holder.subject.setText(model.getCourse());
        holder.Gpa.setText("Gpa: "+(model.getGpa()));



    }

    @Override
    public int getItemCount() {
        return listResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Marks,Gpa,subject,Grade;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Marks=itemView.findViewById(R.id.marks);
            Gpa=itemView.findViewById(R.id.gpa);
            subject=itemView.findViewById(R.id.CourseName);
            Grade=itemView.findViewById(R.id.grade);

        }
    }
}
