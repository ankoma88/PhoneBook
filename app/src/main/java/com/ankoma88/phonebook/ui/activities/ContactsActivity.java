package com.ankoma88.phonebook.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.callbacks.ContactsCallback;
import com.ankoma88.phonebook.models.Contact;
import com.ankoma88.phonebook.ui.fragments.ContactsFragment;
import com.ankoma88.phonebook.ui.fragments.LoginFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ContactsCallback {

    public static final String TAG = ContactsActivity.class.getSimpleName();
    private static final String TAG_CONTACTS_FRAGMENT = "contactsFragment";
    private static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    private static final int REQUEST_NEW_CONTACT = 1;
    private static final int REQUEST_EDIT_CONTACT = 2;

    private SharedPreferences appPrefs;

    private Fragment fragment;

    private Menu navMenu;
    private MenuItem loginItem;
    private MenuItem logoutItem;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPrefs = getSharedPreferences(getString(R.string.appsettings), Context.MODE_PRIVATE);

        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setFabIcon(isLoggedIn());

        fab.setOnClickListener(view -> {
            if (!isLoggedIn()) {
                ContactsActivity.this.showLoginFragment();
            } else {
                ContactsActivity.this.addNewContact();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navMenu = navigationView.getMenu();
        loginItem = navMenu.findItem(R.id.login);
        logoutItem = navMenu.findItem(R.id.logout);

        showContactsFragment(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoginOrLogoutItemInMenu(isLoggedIn());
    }

    private void addNewContact() {
        Intent newContactIntent = new Intent(this, ContactActivity.class);
        newContactIntent.putExtra(ContactActivity.EXTRA_NEW_CONTACT, true);
        startActivityForResult(newContactIntent, REQUEST_NEW_CONTACT);
    }

    @Override
    public void editContact(Contact contact) {
        Intent editContactIntent = new Intent(this, ContactActivity.class);
        editContactIntent.putExtra(ContactActivity.EXTRA_CONTACT, contact);
        startActivityForResult(editContactIntent, REQUEST_EDIT_CONTACT);
    }

    @Override
    public void openContact(Contact contact) {
        Intent openContactIntent = new Intent(this, ContactActivity.class);
        openContactIntent.putExtra(ContactActivity.EXTRA_CONTACT, contact);
        openContactIntent.putExtra(ContactActivity.EXTRA_SHOW, true);
        startActivity(openContactIntent);
    }

    private void showLoginFragment() {
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_FRAGMENT);
        if (fragment == null) {
            fragment = new LoginFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, fragment, TAG_LOGIN_FRAGMENT)
                .addToBackStack(null)
                .commit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void showContactsFragment(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            fragment = new ContactsFragment();
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTACTS_FRAGMENT);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, fragment, TAG_CONTACTS_FRAGMENT)
                .commit();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.login) {
            showLoginFragment();
        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.about) {
            openAboutActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        setLogin(false);
        showLoginOrLogoutItemInMenu(false);
        ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_CONTACTS_FRAGMENT);
        contactsFragment.setAdapter(null);
        contactsFragment.showEmptyView(View.VISIBLE);
    }

    @Override
    public void setLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = appPrefs.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();

        showLoginOrLogoutItemInMenu(isLoggedIn);
    }

    @Override
    public boolean isLoggedIn() {
        return appPrefs.getBoolean(IS_LOGGED_IN, false);
    }

    @Override
    public void showFab(boolean show) {
        if (show) {
            fab.show();
        } else fab.hide();
    }

    @Override
    public void showLoginOrLogoutItemInMenu(boolean isLoggedIn) {
        loginItem.setVisible(!isLoggedIn);
        logoutItem.setVisible(isLoggedIn);

        setFabIcon(isLoggedIn);
    }

    @Override
    public void hideLoginAndLogoutItems() {
        loginItem.setVisible(false);
        logoutItem.setVisible(false);
    }


    private void setFabIcon(boolean isLoggedIn) {
        if (isLoggedIn) {
            fab.setImageResource(R.drawable.ic_add_contact);
        } else {
            fab.setImageResource(R.drawable.ic_menu_login);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_CONTACTS_FRAGMENT);

        switch (requestCode) {
            case REQUEST_NEW_CONTACT:
                contactsFragment.addNewContactAndRefresh(data.getParcelableExtra(ContactActivity.EXTRA_CONTACT));
                break;
            case REQUEST_EDIT_CONTACT:
                contactsFragment.updateContactAndRefresh(data.getParcelableExtra(ContactActivity.EXTRA_CONTACT));
                break;
        }
    }


    private void openAboutActivity() {
        Intent openAboutIntent = new Intent(this, AboutActivity.class);
        startActivity(openAboutIntent);
    }

}

