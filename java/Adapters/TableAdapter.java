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

import com.mukarram.superioruniversity.R;
import com.mukarram.superioruniversity.TimeTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Model.Time_Table;
import Model.Time_Table_Model;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    public TableAdapter(Context mContext, ArrayList<Time_Table> listTable) {
        this.mContext = mContext;
        this.listTable=listTable;
    }

    Context mContext;
    ArrayList<Time_Table> listTable;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.timet_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Time_Table model=listTable.get(position);

        holder.days.setText(model.getDay());
        holder.Time.setText(model.getStartTime()+" - "+model.getEndTime());
        holder.subject.setText(model.getSubject());
        holder.room.setText(model.getRoom());
        holder.teacher.setText(model.getTeacher());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,TimeTable.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView days,room,subject,teacher,Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            days=itemView.findViewById(R.id.day);
            room=itemView.findViewById(R.id.room);
            subject=itemView.findViewById(R.id.subject);
            teacher=itemView.findViewById(R.id.teacher);
            Time=itemView.findViewById(R.id.time);



        }
    }
}
