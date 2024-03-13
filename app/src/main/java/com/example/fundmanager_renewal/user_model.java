package com.example.fundmanager_renewal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class user_model {
    @SerializedName("user_index")
    @Expose
    private long user_index;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("account")
    @Expose
    private String account;

    public user_model(String name, String id, String pw, String email, String account){
        this.username = name;
        this.id = id;
        this.password = pw;
        this.email = email;
        this.account = account;
    }


    public long getUserIndex(){
        return user_index;
    }

    public String getUsername() {
        return username;
    }

    public String getID(){
        return id;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getAccount() {
        return account;
    }
}
