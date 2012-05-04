package com.android.flash.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * User: johnwright
 * Date: 4/15/12
 * Time: 12:01 PM
 */
public class sibDataSource implements DataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;

    public sibDataSource(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    @Override
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        database.close();
    }

    @Override
    public <T> T create(T item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T get(int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
