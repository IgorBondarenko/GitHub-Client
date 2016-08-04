package com.nodeads.test_task.github_client.core.injection;

import com.nodeads.test_task.github_client.activity.MainActivity;
import com.nodeads.test_task.github_client.activity.UserActivity;
import com.nodeads.test_task.github_client.core.adapters.RepositoryAdapter;
import com.nodeads.test_task.github_client.core.adapters.UserAdapter;

import dagger.Component;

/**
 * Created by Igor on 03.08.2016.
 */

@Component(modules = Module.class)
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(UserActivity activity);
    void inject(UserAdapter adapter);
    void inject(RepositoryAdapter adapter);

}
