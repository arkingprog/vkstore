package com.example.arking.vkstore;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName="vkstore";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  user_id INTEGER UNIQUE);");

        db.execSQL("CREATE TABLE favorite_store (\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  group_id INTEGER,\n" +
                "  user_id INTEGER,\n" +
                "  FOREIGN KEY (user_id) REFERENCES user(user_id),\n" +
                "  FOREIGN KEY (group_id) REFERENCES catalog_store(group_id));");

        db.execSQL("CREATE TABLE catalog_store(\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  group_id INTEGER UNIQUE);");

        db.execSQL("CREATE TABLE contact_message(\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  user_contact_id INTEGER UNIQUE);");

        db.execSQL("CREATE TABLE user_contact(\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  user_id INTEGER,\n" +
                "  contact_id INTEGER,\n" +
                "  FOREIGN KEY (user_id) REFERENCES user(user_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
