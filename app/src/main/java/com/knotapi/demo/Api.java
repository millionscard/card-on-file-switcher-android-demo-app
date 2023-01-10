package com.knotapi.demo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {
    String BASE_URL = "https://sample.knotapi.com/api/";

    @POST("register")
    Call<CreateUserResponse> createUserAPI(@Body CreateUserRequest createUserRequest);

    @POST("knot/session")
    Call<CreateSessionResponse> createSessionAPI(@Header("Authorization") String token, @Body CreateSession createSession);
}
