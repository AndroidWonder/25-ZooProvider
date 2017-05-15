package com.course.example.zooprovider;

import android.net.Uri;

public final class Animal {

    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    public static final String MIME_ITEM = "vnd.animal";
    public static final String MIME_TYPE_SINGLE = Animal.MIME_ITEM_PREFIX + "/" + Animal.MIME_ITEM;
    public static final String MIME_TYPE_MULTIPLE = Animal.MIME_DIR_PREFIX + "/" + Animal.MIME_ITEM;

    public static final String AUTHORITY = "com.course.animal";
    public static final String PATH_MULTIPLE = "animals";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Animal.AUTHORITY + "/" + Animal.PATH_MULTIPLE);

    public static final String DEFAULT_SORT_ORDER = "updated DESC";

    public static final String NAME = "name";
    public static final String QUANTITY = "quantity";
    public static final String DATABASE_NAME = "zoo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String ZOO_TABLE = "animals";

    public static final String KEY_ID = "_id";
    
	public static final String TAG = "ZooProvider";
    
}
