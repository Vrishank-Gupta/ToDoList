package com.vrishankgupta.todolistfile.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vrishankgupta.todolistfile.R;

import java.util.ArrayList;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    Context context;
    ArrayList<listClass> tasks;
    public CourseAdapter(Context context, ArrayList<listClass> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(viewType,parent,false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.detail;
    }

    @Override
    public void onBindViewHolder(final CourseViewHolder holder, final int position) {
        final listClass task = tasks.get(position);

        holder.tv.setText(task.getTask());
        holder.cb.setChecked(task.isActive());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setActive(holder.cb.isChecked());
                notifyItemChanged(position);
                new CourseAdapter(context,tasks);
//                Toast.makeText(get,task.getTask() + " is " +task.isActive(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.delCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(task);
                notifyItemChanged(position);
                new CourseAdapter(context,tasks);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        CheckBox cb;
        ImageButton delCb;

        public CourseViewHolder(View converView) {
            super(converView);
            tv = converView.findViewById(R.id.listtv);
            cb = converView.findViewById(R.id.activeCb);
            delCb = converView.findViewById(R.id.delCb);
        }
    }
}