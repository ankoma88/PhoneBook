package com.ankoma88.phonebook.ui.fragments;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.callbacks.ContactsCallback;
import com.ankoma88.phonebook.db.LoginAuthDAO;
import com.ankoma88.phonebook.models.LoginAuth;
import com.ankoma88.phonebook.ui.activities.ContactsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment{
    private static final String TAG = LoginFragment.class.getSimpleName();

    private ContactsCallback callback;
    private LoginAuthDAO loginAuthDAO;

    private View rootView;

    @Bind(R.id.input_username)
    EditText etUsername;

    @Bind(R.id.input_password)
    EditText etPassword;

    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ContactsCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.showFab(false);
        callback.hideLoginAndLogoutItems();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        loginAuthDAO = new LoginAuthDAO(getContext());

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            validateCredentials(username, password);
        });

        return rootView;
    }

    private void validateCredentials(String username, String password) {
        LoginAuth admin = loginAuthDAO.getLoginAuth(getString(R.string.admin));
        if (admin == null) {
            Snackbar.make(rootView, getString(R.string.error_login), Snackbar.LENGTH_SHORT).show();
            return;
        }

        boolean isLoggedIn = (username.equals(admin.getUsername()) && password.equals(admin.getPassword()));
        callback.setLogin(isLoggedIn);
        if (!isLoggedIn)
            Snackbar.make(rootView, getString(R.string.error_login), Snackbar.LENGTH_SHORT).show();
        else {
            ((ContactsActivity)getActivity()).showContactsFragment(null);
        }
    }
}
