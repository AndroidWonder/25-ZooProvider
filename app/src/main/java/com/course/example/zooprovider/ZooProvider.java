package com.course.example.zooprovider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ZooProvider extends ContentProvider {

	private SQLiteDatabase zooDB;
	
	// Create the constants used to differentiate between the different URI requests.
	  private static final int ANIMALS = 1;
	  private static final int ANIMAL_ID = 2;

	  private static final UriMatcher uriMatcher;
	 
	  static {
	   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	   uriMatcher.addURI(Animal.AUTHORITY, "animals", ANIMALS);      //all records of a table
	   uriMatcher.addURI(Animal.AUTHORITY, "animals/#", ANIMAL_ID);  //specific record number
	  }

  @Override
  public boolean onCreate() {
    Context context = getContext();

    //create instance of helper method
    zooDatabaseHelper dbHelper = new zooDatabaseHelper(context, Animal.DATABASE_NAME, 
                                 null, Animal.DATABASE_VERSION);

      //get database
    zooDB = dbHelper.getWritableDatabase();
      if (zooDB == null) return false;
      return true;
  }
    
  @Override
  public Cursor query(Uri uri, 
                      String[] projection, 
                      String selection, 
                      String[] selectionArgs, 
                      String sort) {
        
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    qb.setTables(Animal.ZOO_TABLE);

    // If this is a row query, limit the result set to the passed in row. 
    switch (uriMatcher.match(uri)) {
      case ANIMAL_ID: qb.appendWhere(Animal.KEY_ID + "=" + uri.getPathSegments().get(1));
                     break;
      default      : break;
    }

    // If no sort order is specified sort by date / time
    String orderBy;
    if (TextUtils.isEmpty(sort)) {
      orderBy = Animal.NAME;
    } else {
      orderBy = sort;
    }

    // Apply the query to the underlying database.
    Cursor c = qb.query(zooDB, 
                        projection, 
                        selection, selectionArgs, 
                        null, null, 
                        orderBy);

    // Register the contexts ContentResolver to be notified if
    // the cursor result set changes. 
    c.setNotificationUri(getContext().getContentResolver(), uri);
    
    // Return a cursor to the query result.
    return c;
  }

  @Override
  public Uri insert(Uri _uri, ContentValues _initialValues) {
    // Insert the new row, will return the row number if 
    // successful.
    long rowID = zooDB.insert(Animal.ZOO_TABLE, null, _initialValues);
          
    // Return a URI to the newly inserted row on success.
    if (rowID > 0) {
      Uri uri = ContentUris.withAppendedId(Animal.CONTENT_URI, rowID);
      getContext().getContentResolver().notifyChange(uri, null);
      return uri;
    }
    throw new SQLException("Failed to insert row into " + _uri);
  }

  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    int count;
    
    switch (uriMatcher.match(uri)) {
      case ANIMALS:
        count = zooDB.delete(Animal.ZOO_TABLE, where, whereArgs);
        break;

      case ANIMAL_ID:
        String segment = uri.getPathSegments().get(1);
        count = zooDB.delete(Animal.ZOO_TABLE, Animal.KEY_ID + "="
                                    + segment
                                    + (!TextUtils.isEmpty(where) ? " AND (" 
                                    + where + ')' : ""), whereArgs);
        break;

      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
    int count;
    switch (uriMatcher.match(uri)) {
      case ANIMALS: count = zooDB.update(Animal.ZOO_TABLE, values, 
                                               where, whereArgs);
                   break;

      case ANIMAL_ID: String segment = uri.getPathSegments().get(1);
                     count = zooDB.update(Animal.ZOO_TABLE, values, Animal.KEY_ID 
                             + "=" + segment 
                             + (!TextUtils.isEmpty(where) ? " AND (" 
                             + where + ')' : ""), whereArgs);
                     break;

      default: throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }
 
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case ANIMALS: return Animal.MIME_TYPE_MULTIPLE;
      case ANIMAL_ID: return Animal.MIME_TYPE_SINGLE;
      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }
  
}