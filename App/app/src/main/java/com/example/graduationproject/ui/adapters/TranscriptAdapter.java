package com.example.graduationproject.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.data.remote.Transcript;

import java.util.List;

public class TranscriptAdapter extends RecyclerView.Adapter<TranscriptAdapter.MyViewHolder> {
    Context context;
    List<Transcript.StudentGrade> studentGradeList;
    public TranscriptAdapter(Context context, List<Transcript.StudentGrade> studentGradeList) {
        this.context = context;
        this.studentGradeList = studentGradeList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new TranscriptAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view
        holder.textView1.setText(studentGradeList.get(position).getName());
        holder.textView2.setText(String.valueOf(studentGradeList.get(position).getStudentId()));
        holder.textView3.setText(String.valueOf(studentGradeList.get(position).getGrade()));
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return studentGradeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grabbing all the views from our recycler_view_row layout file
        // kinda like in the onCreate method
        TextView textView1, textView2, textView3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }

    // Method to get the data list
    public List<Transcript.StudentGrade> getDataList() {
        return studentGradeList;
    }
}
