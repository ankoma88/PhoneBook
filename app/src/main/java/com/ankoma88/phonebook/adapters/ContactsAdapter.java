package com.ankoma88.phonebook.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankoma88.phonebook.R;
import com.ankoma88.phonebook.callbacks.OnContactClickListener;
import com.ankoma88.phonebook.models.Contact;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    public static final String TAG = ContactsAdapter.class.getSimpleName();

    private List<Contact> contacts = new ArrayList<>();
    private Context context;
    private OnContactClickListener listener;

    public ContactsAdapter(List<Contact> contacts, Context context, OnContactClickListener listener) {
        if (contacts != null) {
            this.contacts = contacts;
        }
        this.context = context;
        this.listener = listener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivPhoto)
        public SimpleDraweeView ivPhoto;
        @Bind(R.id.tvName)
        public TextView tvName;
        @Bind(R.id.tvPhone)
        public TextView tvPhone;
        @Bind(R.id.ivCall)
        public ImageView ivCall;
        @Bind(R.id.ivSMS)
        public ImageView ivSMS;

        public View view;

        public ContactViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Contact contact) {
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhone());

            String photoUri = contact.getPhoto();
            if (photoUri != null && !photoUri.isEmpty()) {
                ivPhoto.setImageURI(Uri.parse(contact.getPhoto()));
            }

            view.setOnClickListener(v -> listener.onContactClicked(contact));

            view.setOnLongClickListener(v -> {
                listener.onContactLongClicked(v, contact);
                return true;
            });

            ivCall.setOnClickListener(v -> listener.onCallClicked(contact));
            ivSMS.setOnClickListener(v -> listener.onSmsClicked(contact));

        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


}
