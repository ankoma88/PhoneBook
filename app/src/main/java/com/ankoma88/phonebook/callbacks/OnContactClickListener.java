package com.ankoma88.phonebook.callbacks;

import android.view.View;

import com.ankoma88.phonebook.models.Contact;

/**
 * Created by akoma on 27.08.2016.
 */
public interface OnContactClickListener {
    void onContactLongClicked(View v, Contact contact);
    void onCallClicked(Contact contact);
    void onSmsClicked(Contact contact);
    void onContactClicked(Contact contact);
}
