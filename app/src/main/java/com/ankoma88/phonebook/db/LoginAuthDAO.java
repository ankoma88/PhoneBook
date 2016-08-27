package com.ankoma88.phonebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ankoma88.phonebook.models.LoginAuth;

public class LoginAuthDAO extends EntityDBDAO {
    public static final String TAG = "LoginAuthDAO";
    private static final String WHERE_USERNAME_EQUALS = DBHelper.USER_NAME_COLUMN + " = ?";

    public LoginAuthDAO(Context context) {
        super(context);
    }

    public void clear() {
        String sql = "DELETE FROM " + DBHelper.LOGIN_AUTH_TABLE;
        database.execSQL(sql);
    }

    public long save(LoginAuth loginAuth) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.USER_NAME_COLUMN, loginAuth.getUsername());
        values.put(DBHelper.PASSWORD_COLUMN, loginAuth.getPassword());
        return database.insert(DBHelper.LOGIN_AUTH_TABLE, null, values);
    }

    public LoginAuth getLoginAuth(String username) {
        LoginAuth loginAuth = null;

        String sql = "SELECT * FROM " + DBHelper.LOGIN_AUTH_TABLE +
                " WHERE " + WHERE_USERNAME_EQUALS;
        Cursor cursor = database.rawQuery(sql, new String[]{username + ""});

        if (cursor.moveToNext()) {
            loginAuth = new LoginAuth();
            loginAuth.setId(cursor.getInt(0));
            loginAuth.setUsername(cursor.getString(1));
            loginAuth.setPassword(cursor.getString(2));
        }
        return loginAuth;
    }


}
