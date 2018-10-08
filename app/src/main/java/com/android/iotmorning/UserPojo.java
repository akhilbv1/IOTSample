package com.android.iotmorning;

import android.os.Parcel;
import android.os.Parcelable;


public class UserPojo implements Parcelable {

    public static final Parcelable.Creator<UserPojo> CREATOR = new Parcelable.Creator<UserPojo>() {
        @Override
        public UserPojo createFromParcel(Parcel source) {
            return new UserPojo(source);
        }

        @Override
        public UserPojo[] newArray(int size) {
            return new UserPojo[size];
        }
    };
    private String email, password, username, expId;
    private int age;

    public UserPojo() {
    }

    protected UserPojo(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.username = in.readString();
        this.expId = in.readString();
        this.age = in.readInt();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.username);
        dest.writeString(this.expId);
        dest.writeInt(this.age);
    }
}
