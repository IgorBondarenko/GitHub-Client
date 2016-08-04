package com.nodeads.test_task.github_client.core;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Igor on 03.08.2016.
 */
public class GitHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(2)
                .build();
        ImageLoader.getInstance().init(config);

    }
}
