package com.android.flash.util;

/**
 * User: johnwright
 * Date: 4/14/12
 * Time: 11:51 PM
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.flash.SibOne;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //tables
    public static final String TABLE_SIBONE = "sibone";
    public static final String TABLE_SIBTWO = "sibtwo";
    public static final String TABLE_SIBONE_VERBS = "siboneverbs";
    public static final String TABLE_SIBTWO_VERBS = "sibtwoverbs";

    //columns for table:sibone
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SIBTWO = "sibtwo";

    //columns for table:sibtwo
    //COLUMN_ID
    //COLUMN_NAME

    //columns for table:siboneverbs
    //COLUMN_ID
    //COLUMN_NAME
    //COLUMN_DATE
    //COLUMN_SIBTWO

    //columns for table:sibtwoverbs
    //COLUMN_ID
    //COLUMN_NAME

    private static final String DATABASE_NAME = "flash.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SIBONE = "create table "
            + TABLE_SIBONE + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DATE + " DATETIME DEFAULT NULL, "
            + COLUMN_SIBTWO + " INT "
            + ");";

    private static final String DATABASE_CREATE_SIBTWO = "create table "
            + TABLE_SIBTWO + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + ");";

    private static final String DATABASE_CREATE_SIBONE_VERBS = "create table "
            + TABLE_SIBONE_VERBS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DATE + " DATETIME DEFAULT NULL, "
            + COLUMN_SIBTWO + " INT "
            + ");";

    private static final String DATABASE_CREATE_SIBTWO_VERBS = "create table "
            + TABLE_SIBTWO_VERBS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + ");";

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //this is called if the database doesn't exist yet. create it
        database.execSQL(DATABASE_CREATE_SIBONE);
        database.execSQL(DATABASE_CREATE_SIBTWO);
        database.execSQL(DATABASE_CREATE_SIBONE_VERBS);
        database.execSQL(DATABASE_CREATE_SIBTWO_VERBS);

        ArrayList<SibOne> myItems;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.w(MySQLiteHelper.class.getName(),
//                "Upgrading database from version " + oldVersion + " to "
//                        + newVersion + ", which will destroy all old data");
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
//        onCreate(db);
    }

}
