package com.ankoma88.phonebook.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.adapters.ContactsAdapter;
import com.ankoma88.phonebook.callbacks.ContactsCallback;
import com.ankoma88.phonebook.callbacks.OnContactClickListener;
import com.ankoma88.phonebook.db.ContactDAO;
import com.ankoma88.phonebook.models.Contact;
import com.ankoma88.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnContactClickListener {
    private static final String TAG = ContactsFragment.class.getSimpleName();

    private static final String PEOPLE = "peopleData";

    private ContactsCallback callback;
    private ContactDAO contactDAO;
    private ArrayList<Contact> contactList;

    @Bind(R.id.empty_view)
    View emptyView;

    @Bind(R.id.rvContacts)
    RecyclerView rvContacts;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

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
        callback.showFab(true);

        if (callback.isLoggedIn()) {
            callback.showLoginOrLogoutItemInMenu(true);

            if (contactList==null || contactList.isEmpty()) {
                loadContactsFromDB();
            } else {
                setAdapter(contactList);
            }

        } else {
            showEmptyView(View.VISIBLE);
            callback.showLoginOrLogoutItemInMenu(false);
        }

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public void showEmptyView(int visible) {
        emptyView.setVisibility(visible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactDAO = new ContactDAO(getContext());
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!callback.isLoggedIn()) {
            return;
        }
        if (savedInstanceState != null) {
            contactList = savedInstanceState.getParcelableArrayList(PEOPLE);
            setAdapter(contactList);
        } else {
            loadContactsFromDB();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PEOPLE, contactList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvContacts.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    private void loadContactsFromDB() {
        new LoadContactsTask().execute();
    }

    @Override
    public void onRefresh() {
        if (!callback.isLoggedIn()) {
            return;
        }
        loadContactsFromDB();
    }

    public void setAdapter(List<Contact> contactArrayList) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

        if (contactArrayList == null) {
            contactArrayList = new ArrayList<>();
        }
        final ContactsAdapter adapter = new ContactsAdapter(contactArrayList, getActivity() ,this);
        rvContacts.setAdapter(adapter);
    }

    @Override
    public void onContactClicked(Contact contact) {
        callback.openContact(contact);
    }

    @Override
    public void onContactLongClicked(View view, Contact contact) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.CENTER);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteContactAndRefresh(contact);
                    return true;
                case R.id.action_edit:
                    editContact(contact);
                    return true;
                default:return false;
            }
        });
        popupMenu.inflate(R.menu.menu_contact);
        popupMenu.show();
    }

    @Override
    public void onCallClicked(Contact contact) {
        Utils.openDialer(contact, getContext());
    }

    @Override
    public void onSmsClicked(Contact contact) {
        Utils.openSms(contact, getContext());
    }

    private void editContact(Contact contact) {
        callback.editContact(contact);
    }

    public void addNewContactAndRefresh(Contact newContact) {
        contactDAO.save(newContact);
        loadContactsFromDB();
    }

    private void deleteContactAndRefresh(Contact contact) {
        contactDAO.delete(contact);
        loadContactsFromDB();
    }

    public void updateContactAndRefresh(Contact updContact) {
        contactDAO.update(updContact);
        loadContactsFromDB();
    }

    private class LoadContactsTask extends AsyncTask<Void, Void, List<Contact>> {
        @Override
        protected List<Contact> doInBackground(Void... params) {
           return contactDAO.getContacts();
        }

        @Override
        protected void onPostExecute(List<Contact> contactList) {
            super.onPostExecute(contactList);
            showEmptyView(View.GONE);

            if (!contactList.isEmpty()) {
                setAdapter(contactList);
            }
        }
    }


}
