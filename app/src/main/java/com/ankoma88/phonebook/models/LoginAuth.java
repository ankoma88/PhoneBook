package com.ankoma88.phonebook.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginAuth implements Parcelable {
    private int id;
    private String username;
    private String password;

    public LoginAuth() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginAuth loginAuth = (LoginAuth) o;

        if (id != loginAuth.id) return false;
        if (!username.equals(loginAuth.username)) return false;
        return password.equals(loginAuth.password);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoginAuth{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
    }

    protected LoginAuth(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<LoginAuth> CREATOR = new Creator<LoginAuth>() {
        @Override
        public LoginAuth createFromParcel(Parcel in) {
            return new LoginAuth(in);
        }

        @Override
        public LoginAuth[] newArray(int size) {
            return new LoginAuth[size];
        }
    };
}
