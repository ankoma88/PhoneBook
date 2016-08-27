package com.ankoma88.phonebook.ui.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.models.Contact;
import com.ankoma88.phonebook.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactActivity extends AppCompatActivity {
    public static final String TAG = ContactActivity.class.getSimpleName();

    public static final String EXTRA_CONTACT = "extraContactEdit";
    public static final int REQUEST_PHOTO = 3;
    public static final String EXTRA_SHOW = "extraShowContact";
    public static final String EXTRA_NEW_CONTACT = "extraNewContact";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ivPhoto)
    SimpleDraweeView ivPhoto;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etWebsite)
    EditText etWebsite;
    @Bind(R.id.btnDone)
    Button btnDone;
    @Bind(R.id.llButtons)
    LinearLayout llButtons;
    @Bind(R.id.ivCall)
    ImageView ivCall;
    @Bind(R.id.ivSMS)
    ImageView ivSms;

    private Contact selectedContact;
    private Contact updatedContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedContact = bundle.getParcelable(EXTRA_CONTACT);
            if (selectedContact != null) {
                showContactData();
            }

            if (bundle.getBoolean(EXTRA_SHOW)) {
                forbidEdit();
            }

            if (bundle.getBoolean(EXTRA_NEW_CONTACT)) {
                llButtons.setVisibility(View.GONE);
            }
        }

        updatedContact = new Contact();

        ivPhoto.setOnClickListener(v -> findPhoto());

        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent();
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String website = etWebsite.getText().toString();

            updatedContact.setName(name);
            updatedContact.setPhone(phone);
            updatedContact.setEmail(email);
            updatedContact.setWebsite(website);

            if (selectedContact != null) {
                updatedContact.setId(selectedContact.getId());
            }

            if (updatedContact.getPhoto() == null) {
                updatedContact.setPhoto(selectedContact.getPhoto());
            }

            intent.putExtra(EXTRA_CONTACT, updatedContact);

            setResult(RESULT_OK, intent);
            finish();
        });

        ivCall.setOnClickListener(v -> Utils.openDialer(selectedContact, ContactActivity.this));
        ivSms.setOnClickListener(v -> Utils.openSms(selectedContact, ContactActivity.this));
    }

    private void forbidEdit() {
        btnDone.setVisibility(View.GONE);
        etName.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);
        etWebsite.setEnabled(false);
        ivPhoto.setEnabled(false);
    }

    private void showContactData() {
        etName.setText(selectedContact.getName());
        etPhone.setText(selectedContact.getPhone());
        etEmail.setText(selectedContact.getEmail());
        etWebsite.setText(selectedContact.getWebsite());
        ivPhoto.setImageURI(selectedContact.getPhoto());
    }

    private void findPhoto() {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                updatedContact.setPhoto(String.valueOf(photoUri));
                ivPhoto.setImageURI(photoUri);
            } else {
                ivPhoto.setImageURI(selectedContact.getPhoto());
                updatedContact.setPhoto(selectedContact.getPhoto());
            }
        }
    }


}
