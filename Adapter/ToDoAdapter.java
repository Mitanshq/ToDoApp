package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.addNewTask;
import com.example.todoapp.modal.ToDoModal;
import com.example.todoapp.utils.databaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.myViewHolder> {

    private List<ToDoModal> mList;
    private MainActivity activity;
    private databaseHelper myDB;

    public ToDoAdapter(databaseHelper myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final ToDoModal item = mList.get(position);
        holder.mc.setText(item.getTask());
        holder.mc.setChecked(toBoolean(item.getStatus()));
        holder.mc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDB.updateStatus(item.getId(), 1);
                }else{
                    myDB.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<ToDoModal> mlist){
        this.mList = mlist;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModal item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        ToDoModal item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        addNewTask task = new addNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class myViewHolder extends  RecyclerView.ViewHolder{

        CheckBox mc;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mc = itemView.findViewById(R.id.mcheckBox);
        }
    }
}
