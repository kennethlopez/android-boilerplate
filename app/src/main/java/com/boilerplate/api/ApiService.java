package com.boilerplate.api;


import com.boilerplate.data.Follower;
import com.boilerplate.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/users/{username}")
    Call<User> getUser(
            @Path("username") String username
    );

    @GET("/users/{username}/followers")
    Call<List<Follower>> getFollowers(
            @Path("username") String username
    );
}