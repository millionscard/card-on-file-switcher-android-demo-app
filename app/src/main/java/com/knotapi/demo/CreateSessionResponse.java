package com.knotapi.demo;

import com.google.gson.annotations.SerializedName;

public class CreateSessionResponse {

    @SerializedName("session")
    private String session;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
