package com.mintminter.simpletodo.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irene on 8/21/17.
 */

public class EventDepotHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String TABLE_NAME = "eventdepot";
    protected static final String COLUMN_CREATETIME = "ctime";
    protected static final String COLUMN_CONTENT = "content";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CREATETIME + " INTEGER," +
                    COLUMN_CONTENT + " TEXT);" ;

    public EventDepotHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
