package com.mintminter.simpletodo.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Irene on 8/21/17.
 */

public class EventDepot {
    private SQLiteDatabase mDB;
    private EventDepotHelper mDBHelper;
    private String[] allColumns = {EventDepotHelper.COLUMN_CREATETIME, EventDepotHelper.COLUMN_CONTENT};

    public EventDepot(Context context){
        mDBHelper = new EventDepotHelper(context);
    }

    public void open(){
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close(){
        mDBHelper.close();
    }

    public void add(Event event){
        ContentValues values = new ContentValues();
        values.put(EventDepotHelper.COLUMN_CREATETIME, event.mCreateTime);
        values.put(EventDepotHelper.COLUMN_CONTENT, event.toString());
        long insertId = mDB.insert(EventDepotHelper.TABLE_NAME, null, values);
    }

    public void delete(Event event){
        mDB.delete(EventDepotHelper.TABLE_NAME,
                EventDepotHelper.COLUMN_CREATETIME + " = " + event.mCreateTime, null);
    }

    public void update(Event event){
        String strFilter = EventDepotHelper.COLUMN_CREATETIME + " = " + event.mCreateTime;
        ContentValues values = new ContentValues();
        values.put(EventDepotHelper.COLUMN_CONTENT, event.toString());
        mDB.update(EventDepotHelper.TABLE_NAME, values, strFilter, null);
    }

    public ArrayList<Event> getAllEvents(){
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = mDB.query(EventDepotHelper.TABLE_NAME, allColumns, null, null, null, null, EventDepotHelper.COLUMN_CREATETIME);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String content = cursor.getString(1);
            Event event = new Event();
            event.fromString(content);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }
}
