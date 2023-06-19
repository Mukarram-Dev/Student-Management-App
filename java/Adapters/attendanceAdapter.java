package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mukarram.superioruniversity.R;

import java.util.ArrayList;

import Model.Attend_Model;

public class attendanceAdapter extends RecyclerView.Adapter<attendanceAdapter.ViewHolder>{

    Context mContext;

    public attendanceAdapter(Context mContext) {
        this.mContext = mContext;

    }




    @NonNull
    @Override
    public attendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attendance_layout, null);
        return new attendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull attendanceAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Image_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            Image_profile = itemView.findViewById(R.id.attend);

        }
    }
}
