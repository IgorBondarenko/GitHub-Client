package com.nodeads.test_task.github_client.core.injection;

import android.content.Context;

import com.nodeads.test_task.github_client.core.controllers.AnimationController;
import com.nodeads.test_task.github_client.core.retrofit.GitHubAPI;

import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Igor on 03.08.2016.
 */

@dagger.Module
public class Module {

    private Context mContext;

    public Module(Context context) {
        this.mContext = context;
    }

    @Provides
    public AnimationController provideAnimationController(){
        return new AnimationController(mContext);
    }

    @Provides
    public GitHubAPI provideGitHubAPI(){
        return new Retrofit.Builder()
                .baseUrl(GitHubAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(GitHubAPI.class);
    }

}
