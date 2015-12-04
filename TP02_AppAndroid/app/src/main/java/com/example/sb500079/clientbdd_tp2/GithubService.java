package com.example.sb500079.clientbdd_tp2;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GithubService {
    @GET("/users/{user}/repos")
    Call<List<Repo>>  listRepos(@Path("user") String user);

   /* @GET("/search/repositories")
    Call<List> searchRepos(@Query("q") String query);*/


}
