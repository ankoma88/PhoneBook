package com.ankoma88.phonebook.callbacks;


import com.ankoma88.phonebook.models.Contact;

public interface ContactsCallback {
    void setLogin(boolean isLoggedIn);
    boolean isLoggedIn();
    void showFab(boolean show);
    void showLoginOrLogoutItemInMenu(boolean isLoggenIn);
    void hideLoginAndLogoutItems();
    void editContact(Contact contact);
    void openContact(Contact contact);
}
