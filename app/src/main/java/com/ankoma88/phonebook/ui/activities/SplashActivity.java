package com.ankoma88.phonebook.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.db.ContactDAO;
import com.ankoma88.phonebook.db.LoginAuthDAO;
import com.ankoma88.phonebook.models.Contact;
import com.ankoma88.phonebook.models.LoginAuth;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Bind(R.id.ivLogo)
    ImageView ivLogo;

    private ContactDAO contactDAO;
    private LoginAuthDAO loginAuthDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactDAO = new ContactDAO(this);
        loginAuthDAO = new LoginAuthDAO(this);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        ivLogo.setAnimation(anim);

        new BackgroundTask().execute();
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        Intent intent;

        @Override
        protected Void doInBackground(Void... params) {

            initAdmin();

            loadSomeContactsFromDeviceToDatabase(getResources()
                    .getInteger(R.integer.default_contacts_number));

            try {
                Thread.sleep(getResources().getInteger(R.integer.min_splash_time));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(SplashActivity.this, ContactsActivity.class);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SplashActivity.this, ivLogo, "logo");
            startActivity(intent, options.toBundle());
            finish();
        }
    }

    private void initAdmin() {
        LoginAuth admin = loginAuthDAO.getLoginAuth(getString(R.string.admin));
        if (admin == null) {
            admin = new LoginAuth();
            admin.setUsername(getString(R.string.admin));
            admin.setPassword(getString(R.string.admin_password));
            loginAuthDAO.save(admin);
        }
    }

    private void loadSomeContactsFromDeviceToDatabase(int realContactsNumber) {
        if (!contactDAO.getContacts().isEmpty()) {
            return;
        }

        final Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            for(int i = 0; i < realContactsNumber; i++) {
                cursor.moveToNext();
                Contact contact = getContactFromContentProvider(cursor);
                contactDAO.save(contact);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private Contact getContactFromContentProvider(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(name);
        contact.setPhone(phoneNumber);
        contact.setPhoto(photo);

        return contact;
    }

}
