package edu.uncc.evaluation05.models;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    String status, user_id, user_fname, user_lname, user_email, token;

    /*
    {
    "status": "ok",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzM3NjAxNzcsImV4cCI6MTc2NTI5NjE3NywianRpIjoiMWgwQTNlZWU5VFBuTm0ycGJ3NjBzbiIsInVzZXIiOjJ9.80hvMaZX6RXXnusUQ9V4cnG4NLN72yyDiIuHuX13nYw",
    "user_id": 2,
    "user_email": "a@a.com",
    "user_fname": "Alice",
    "user_lname": "Smith",
    "user_role": "USER"
}
     */

    public AuthResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
