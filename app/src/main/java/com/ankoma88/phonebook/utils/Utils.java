package com.ankoma88.phonebook.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ankoma88.phonebook.models.Contact;

public class Utils {

    public static void openSms(Contact contact, Context context) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.fromParts("sms", contact.getPhone(), null));
        context.startActivity(sendIntent);
    }

    public static void openDialer(Contact contact, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));
        context.startActivity(intent);
    }

}
