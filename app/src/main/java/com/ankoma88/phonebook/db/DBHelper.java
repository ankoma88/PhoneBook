package com.ankoma88.phonebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contactsDB";
    private static final int DB_VERSION = 1;

    public static final String CONTACTS_TABLE = "contactsTable";
    public static final String LOGIN_AUTH_TABLE = "loginAuthTable";

    public static final String ID_COLUMN = "_id";

    public static final String NAME_COLUMN = "name";
    public static final String PHONE_COLUMN = "phone";
    public static final String PHOTO_COLUMN = "photo";
    public static final String EMAIL_COLUMN = "email";
    public static final String WEBSITE_COLUMN = "website";

    public static final String USER_NAME_COLUMN = "username";
    public static final String PASSWORD_COLUMN = "passworde";

    public static final String CREATE_CONTACTS_TABLE = "CREATE TABLE "
            + CONTACTS_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + NAME_COLUMN + " TEXT, "
            + PHONE_COLUMN + " TEXT, "
            + PHOTO_COLUMN + " TEXT, "
            + EMAIL_COLUMN + " TEXT, "
            + WEBSITE_COLUMN + " TEXT" + ")";

    public static final String CREATE_LOGIN_AUTH_TABLE = "CREATE TABLE "
            + LOGIN_AUTH_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + USER_NAME_COLUMN + " TEXT, "
            + PASSWORD_COLUMN + " TEXT" + ")";

    private static DBHelper instance;

    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_LOGIN_AUTH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}