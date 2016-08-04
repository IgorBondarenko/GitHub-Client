package com.nodeads.test_task.github_client.core.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodeads.test_task.github_client.R;
import com.nodeads.test_task.github_client.core.controllers.AnimationController;
import com.nodeads.test_task.github_client.core.injection.DaggerAppComponent;
import com.nodeads.test_task.github_client.core.injection.Module;
import com.nodeads.test_task.github_client.core.retrofit.enteties.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Igor on 04.08.2016.
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>{

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        TextView repositoryName;
        TextView repositoryLanguage;

        public RepositoryViewHolder(View itemView) {
            super(itemView);
            this.repositoryName = (TextView)itemView.findViewById(R.id.item_repository_name);
            this.repositoryLanguage = (TextView)itemView.findViewById(R.id.item_repository_language);
        }

    }

    @Inject AnimationController mAnimationController;
    private List<Repository> repositories;

    public RepositoryAdapter(Context context, List<Repository> repositories) {
        DaggerAppComponent.builder().module(new Module(context)).build().inject(this);
        this.repositories = repositories;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.repositoryName.setText(repositories.get(position).getName());
        holder.repositoryLanguage.setText(repositories.get(position).getLanguage());
        holder.itemView.startAnimation(mAnimationController.getAnimation(R.anim.slide_in_top));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

}
