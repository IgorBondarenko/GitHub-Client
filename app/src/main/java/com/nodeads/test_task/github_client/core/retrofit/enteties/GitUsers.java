package com.nodeads.test_task.github_client.core.retrofit.enteties;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Igor on 03.08.2016.
 */
public class GitUsers {

    @SerializedName("items")
    private List<User> gitUsers;

    public List<User> getUsers() {
        return gitUsers;
    }

    public class User {

        @SerializedName("login")
        private String name;

        @SerializedName("avatar_url")
        private String avatarUrl;

        @SerializedName("repos_url")
        private String reposUrl;

        public String getName() {
            return name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }
    }

}
