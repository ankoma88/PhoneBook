package com.ankoma88.phonebook.ui.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ankoma88.phonebook.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = AboutActivity.class.getSimpleName();

    public static final String URL_FACEBOOK = "https://www.facebook.com/anton.komarovskyi";
    public static final String URL_GOOGLE_PLUS = "https://plus.google.com/+AntonKmr/posts";
    public static final String URL_LINKEDIN = "https://www.linkedin.com/in/anton-komarovskyi-87780b24";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btnFacebook)
    ImageView btnFacebook;

    @Bind(R.id.btnGooglePlus)
    ImageView btnGooglePlus;

    @Bind(R.id.btnLinkedIn)
    ImageView btnLinkedIn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        btnLinkedIn.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGooglePlus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFacebook:
                openUrl(URL_FACEBOOK);
                break;
            case R.id.btnGooglePlus:
                openUrl(URL_GOOGLE_PLUS);
                break;
            case R.id.btnLinkedIn:
                openUrl(URL_LINKEDIN);
                break;
        }
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
