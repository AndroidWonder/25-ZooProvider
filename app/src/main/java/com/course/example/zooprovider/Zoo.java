/*This Activity acts as a Content Resolver. It is allowed to both read and write to the database
  because of the permissions in the Manifest.
  It adds the initial records into the database.
*/
package com.course.example.zooprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TextView;

public class Zoo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView text = (TextView)findViewById(R.id.Text);
        ContentValues values;
        
        long id = 0;
        String name = null; 
        int quantity = 0;
        
        //the database is located through the uri
        Uri uri = Animal.CONTENT_URI;
        
        //delete animals table content
        getContentResolver().delete(uri, null, null);
        
      //insert values
        values = new ContentValues();
        values.put("name", "tiger");
        values.put("quantity",4);
        getContentResolver().insert(uri, values);
        
        values = new ContentValues();
        values.put("name", "zebra");
        values.put("quantity",23);
        getContentResolver().insert(uri, values);
        
        values = new ContentValues();
        values.put("name", "tiger");
        values.put("quantity",7);
        getContentResolver().insert(uri, values);
        
        values = new ContentValues();
        values.put("name", "rhino");
        values.put("quantity",3);
        getContentResolver().insert(uri, values);
        
        values = new ContentValues();
        values.put("name", "lion");
        values.put("quantity",17);
        getContentResolver().insert(uri, values);
        
        //update buffalo to gorilla
        values = new ContentValues();
    	values.put("name", "gorilla");
    	getContentResolver().update(uri, values, "name=?", new String[] {"buffalo"});
    	
    	//delete tiger
    	getContentResolver().delete(uri, "name=?", new String[] {"tiger"});
            
        //query animals
        Cursor cur = getContentResolver().query(uri, null, null, null, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                
                id = cur.getLong(cur.getColumnIndex(BaseColumns._ID));
                name = cur.getString(cur.getColumnIndex(Animal.NAME));
                quantity = cur.getInt(cur.getColumnIndex(Animal.QUANTITY));
                text.append(name + " " + quantity + " " + "\n");
            }
        }

        cur.close();
    }
}