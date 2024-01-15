package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.modal.ToDoModal;
import com.example.todoapp.utils.databaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class addNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "addNewTask";

    private EditText mEditText;
    private Button mSave;
    private databaseHelper myDB;

    public static addNewTask newInstance(){
        return new addNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mSave = view.findViewById(R.id.button_save);

        myDB = new databaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle =  getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if(task.length() > 0){
                mSave.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    mSave.setEnabled(false);
                    mSave.setBackgroundColor(Color.GRAY);
                }else{
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(getResources().getColor(R.color.primary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                if(finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text);
                }else{
                    ToDoModal item = new ToDoModal();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity  = getActivity();
        if(activity instanceof onDialogCloseListener){
            ((onDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
