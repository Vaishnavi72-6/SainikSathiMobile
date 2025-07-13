package com.example.sainiksathimobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class DependentDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dependents.db";
    private static final int DB_VERSION = 1;

    public DependentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE dependents (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, relation TEXT, dob TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dependents");
        onCreate(db);
    }

    public void insertDependent(String name, String relation, String dob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("relation", relation);
        values.put("dob", dob);
        db.insert("dependents", null, values);
        db.close();
    }

    public List<String> getAllDependents() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM dependents", null);
        if (cursor.moveToFirst()) {
            do {
                String entry = "Name: " + cursor.getString(1) +
                        "\nRelation: " + cursor.getString(2) +
                        "\nDOB: " + cursor.getString(3);
                list.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteDependent(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("dependents", "name = ?", new String[]{name});
        db.close();
    }
}
