package com.nodeads.test_task.github_client.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodeads.test_task.github_client.R;
import com.nodeads.test_task.github_client.core.adapters.UserAdapter;
import com.nodeads.test_task.github_client.core.controllers.AnimationController;
import com.nodeads.test_task.github_client.core.injection.DaggerAppComponent;
import com.nodeads.test_task.github_client.core.injection.Module;
import com.nodeads.test_task.github_client.core.retrofit.GitHubAPI;
import com.nodeads.test_task.github_client.core.retrofit.enteties.GitUsers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.users_rv) RecyclerView mUsersRV;
    @BindView(R.id.user_name_et) EditText mUserNameET;
    @BindView(R.id.no_results_tv) TextView mNoResultsTV;
    @BindView(R.id.main_pb) ProgressBar mProgressBar;
    @Inject AnimationController mAnimationController;
    @Inject GitHubAPI gitHubAPI;

    private List<GitUsers.User> users = new ArrayList<>();
    private UserAdapter mUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DaggerAppComponent.builder().module(new Module(this)).build().inject(this);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            String text = mUserNameET.getText().toString();
            if(!text.isEmpty()){
                updateAdapter(gitHubAPI.getUsers(text));
            } else {
                Toast.makeText(MainActivity.this, R.string.toast_empty_query, Toast.LENGTH_LONG).show();
            }
        });

        mUsersRV.setLayoutManager(new LinearLayoutManager(this));
        mUsersRV.setAdapter(mUsersAdapter = new UserAdapter(this, users));
    }

    private void updateAdapter(Observable<GitUsers> observable){
        mNoResultsTV.setVisibility(View.GONE);
        users.clear();
        mUsersAdapter.notifyDataSetChanged();
        mAnimationController.show(mProgressBar);
        observable
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> {mAnimationController.hide(mProgressBar); mUsersAdapter.notifyDataSetChanged();})
                .map(gitObject -> gitObject.getUsers())
                .filter(gitUsers -> {
                    if(gitUsers != null && gitUsers.size() > 0){
                        return true;
                    } else {
                        mNoResultsTV.setText(MainActivity.this.getString(R.string.no_results, mUserNameET.getText()));
                        mAnimationController.show(mNoResultsTV);
                    }
                    return false;
                })
                .flatMap(gitUsers -> Observable.from(gitUsers))
                .subscribe(
                        user -> users.add(user),
                        e -> {
                            mAnimationController.hide(mProgressBar);
                            Snackbar
                                    .make(findViewById(android.R.id.content), R.string.snackbar_connection_fail, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.snackbar_try_again, v -> {mAnimationController.show(mProgressBar); updateAdapter(observable);}).show();});
    }
}
