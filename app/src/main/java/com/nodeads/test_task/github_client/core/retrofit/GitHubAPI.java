package com.nodeads.test_task.github_client.core.retrofit;

import com.nodeads.test_task.github_client.core.retrofit.enteties.GitUsers;
import com.nodeads.test_task.github_client.core.retrofit.enteties.Repository;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Igor on 03.08.2016.
 */
public interface GitHubAPI {

    String BASE_URL = "https://api.github.com/";

    @GET("/search/users")
    Observable<GitUsers> getUsers(@Query("q") String query);

    @GET
    Observable<ArrayList<Repository>> getRepository(@Url String url);

}
