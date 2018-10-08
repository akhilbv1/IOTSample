package com.android.iotmorning;

import com.google.gson.annotations.SerializedName;


public class LoginBody {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("returnSecureToken")
    private boolean returnSecureToken;

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

    public boolean isReturnSecureToken() {
        return returnSecureToken;
    }

    public void setReturnSecureToken(boolean returnSecureToken) {
        this.returnSecureToken = returnSecureToken;
    }
}
