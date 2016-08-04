package com.nodeads.test_task.github_client.core.retrofit.enteties;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 03.08.2016.
 */
public class Repository {

    @SerializedName("name")
    private String name;

    @SerializedName("language")
    private String language;

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }
}
