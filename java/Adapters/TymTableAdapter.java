package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mukarram.superioruniversity.R;

import java.util.ArrayList;

import Model.Time_Table_Model;

public class TymTableAdapter extends RecyclerView.Adapter<TymTableAdapter.ViewHolder> {


    Context mContext;
    ArrayList<Time_Table_Model> listTable;
    public TymTableAdapter(Context mContext, ArrayList<Time_Table_Model> listTable) {
        this.mContext = mContext;
        this.listTable = listTable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tym_table_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Time_Table_Model model=listTable.get(position);

        holder.days.setText(model.getDay());
        holder.getDay.setText(model.getDay());
        holder.Time.setText(model.getStartTime()+" - "+model.getEndTime());
        holder.subject.setText(model.getSubject());
        holder.room.setText(model.getRoom());
        holder.teacher.setText(model.getTeacher());

    }

    @Override
    public int getItemCount() {
        return listTable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView getDay,days,room,subject,teacher,Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            getDay=itemView.findViewById(R.id.getDay);
            days=itemView.findViewById(R.id.day);
            room=itemView.findViewById(R.id.room);
            subject=itemView.findViewById(R.id.subject);
            teacher=itemView.findViewById(R.id.teacher);
            Time=itemView.findViewById(R.id.time);



        }
    }
}
