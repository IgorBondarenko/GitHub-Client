package com.nodeads.test_task.github_client.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nodeads.test_task.github_client.R;
import com.nodeads.test_task.github_client.core.adapters.RepositoryAdapter;
import com.nodeads.test_task.github_client.core.controllers.AnimationController;
import com.nodeads.test_task.github_client.core.injection.DaggerAppComponent;
import com.nodeads.test_task.github_client.core.injection.Module;
import com.nodeads.test_task.github_client.core.retrofit.GitHubAPI;
import com.nodeads.test_task.github_client.core.retrofit.enteties.Repository;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.repositories_rv) RecyclerView mRepositoriesRV;
    @BindView(R.id.user_name) TextView mUserNameTV;
    @BindView(R.id.no_repositories_tv) TextView mNoRepositoriesTV;
    @BindView(R.id.user_avatar) CircleImageView mUserAvatarTV;
    @Inject AnimationController mAnimationController;
    @Inject GitHubAPI gitHubAPI;

    private List<Repository> repositories = new ArrayList<>();
    private RepositoryAdapter mRepositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DaggerAppComponent.builder().module(new Module(this)).build().inject(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mUserNameTV.setText(getIntent().getStringExtra("user_login"));
        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("user_avatar"), mUserAvatarTV);

        mRepositoriesRV.setLayoutManager(new LinearLayoutManager(this));
        mRepositoriesRV.setAdapter(mRepositoryAdapter = new RepositoryAdapter(this, repositories));

        updateAdapter(gitHubAPI.getRepository(getIntent().getStringExtra("user_repository")));

        final LinearLayout circularLayout = (LinearLayout)findViewById(R.id.circular_reveal_background);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            circularLayout.post(() -> {
                CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
                mAnimationController.circularReveal(circularLayout, collapsingToolbar, R.color.colorPrimary);
            });
        } else {
            circularLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdapter(Observable<ArrayList<Repository>> observable){
        observable
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> mRepositoryAdapter.notifyDataSetChanged())
                .filter(repositories -> {
                    if(repositories != null && repositories.size() > 0){
                        return true;
                    } else {
                        mAnimationController.show(mNoRepositoriesTV);
                    }
                    return false;
                })
                .flatMap(repositories -> Observable.from(repositories))
                .subscribe(
                        repository -> repositories.add(repository),
                        e -> {
                            Toast.makeText(UserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            onBackPressed();
                        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
