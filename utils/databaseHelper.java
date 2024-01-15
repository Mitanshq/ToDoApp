package com.example.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.modal.ToDoModal;

import java.util.ArrayList;
import java.util.List;

public class databaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static  final String DATABASE_NAME = "TODO_DATA";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String col1 = "ID";
    private static  final String col2 = "TASK";
    private static  final String col3 = "STATUS";

    public databaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDoModal model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col2, model.getTask());
        values.put(col3, 0);
        db.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col2, task);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col3, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDoModal> getAllTask(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModal> modelList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModal task = new ToDoModal();
                        task.setId(cursor.getInt(cursor.getColumnIndex(col1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(col2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(col3)));
                        modelList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
             db.endTransaction();
             cursor.close();
        }
        return modelList;
    }
}
