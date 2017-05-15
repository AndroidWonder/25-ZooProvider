package com.course.example.zooprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

//Helper class for opening, creating, and managing database version control
public class zooDatabaseHelper extends SQLiteOpenHelper {
	  
 private static final String TABLE_CREATE =
    "create table " + Animal.ZOO_TABLE + " ("
    + Animal.KEY_ID + " integer primary key autoincrement, "
    + Animal.NAME + " TEXT, "
    + Animal.QUANTITY + " INTEGER); ";
   
     
 public zooDatabaseHelper(Context context, String name,
                                 CursorFactory factory, int version) {
   super(context, name, factory, version);
 }

 @Override
 public void onCreate(SQLiteDatabase db) {
   db.execSQL(TABLE_CREATE);
 }

 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   Log.w(Animal.TAG, "Upgrading database from version " + oldVersion + " to "
               + newVersion + ", which will destroy all old data");
           
   db.execSQL("DROP TABLE IF EXISTS " + Animal.ZOO_TABLE);
   onCreate(db);
 }
}