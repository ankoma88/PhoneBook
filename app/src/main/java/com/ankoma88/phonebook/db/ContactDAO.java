package com.ankoma88.phonebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ankoma88.phonebook.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDAO extends EntityDBDAO {
    public static final String TAG = "ContactDAO";
    private static final String WHERE_ID_EQUALS = DBHelper.ID_COLUMN + " = ?";

    public ContactDAO(Context context) {
        super(context);
    }

    public void populateDB(List<Contact> contacts) {
        clear();
        ArrayList<Contact> contactList = (ArrayList<Contact>) contacts;
        for (Contact contact : contactList) {
            save(contact);
        }
    }

    public void clear() {
        String sql = "DELETE FROM " + DBHelper.CONTACTS_TABLE;
        database.execSQL(sql);
    }

    public long update(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME_COLUMN, contact.getName());
        values.put(DBHelper.PHONE_COLUMN, contact.getPhone());
        values.put(DBHelper.PHOTO_COLUMN, contact.getPhoto());
        values.put(DBHelper.EMAIL_COLUMN, contact.getEmail());
        values.put(DBHelper.WEBSITE_COLUMN, contact.getWebsite());
        long result = database.update(
                DBHelper.CONTACTS_TABLE,
                values,
                WHERE_ID_EQUALS,
                new String[]{ ""+String.valueOf(contact.getId()) });
        return result;

    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.CONTACTS_TABLE,
                new String[]{DBHelper.ID_COLUMN,
                        DBHelper.NAME_COLUMN,
                        DBHelper.PHONE_COLUMN,
                        DBHelper.PHOTO_COLUMN,
                        DBHelper.EMAIL_COLUMN,
                        DBHelper.WEBSITE_COLUMN
                },
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setPhoto(cursor.getString(3));
            contact.setEmail(cursor.getString(4));
            contact.setWebsite(cursor.getString(5));
            contacts.add(contact);
        }
        return contacts;
    }

    public long save(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME_COLUMN, contact.getName());
        values.put(DBHelper.PHONE_COLUMN, contact.getPhone());
        values.put(DBHelper.PHOTO_COLUMN, contact.getPhoto());
        values.put(DBHelper.EMAIL_COLUMN, contact.getEmail());
        values.put(DBHelper.WEBSITE_COLUMN, contact.getWebsite());
        return database.insert(DBHelper.CONTACTS_TABLE, null, values);
    }

    public int delete(Contact contact) {
        return database.delete(DBHelper.CONTACTS_TABLE, WHERE_ID_EQUALS,
                new String[]{contact.getId() + ""});
    }

    public Contact getContact(long id) {
        Contact contact = null;

        String sql = "SELECT * FROM " + DBHelper.CONTACTS_TABLE +
                " WHERE " + DBHelper.ID_COLUMN + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{id + ""});

        if (cursor.moveToNext()) {
            contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setPhoto(cursor.getString(3));
            contact.setEmail(cursor.getString(4));
            contact.setWebsite(cursor.getString(5));
        }
        return contact;
    }


}
