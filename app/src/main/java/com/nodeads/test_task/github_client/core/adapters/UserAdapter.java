package com.nodeads.test_task.github_client.core.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodeads.test_task.github_client.R;
import com.nodeads.test_task.github_client.core.controllers.AnimationController;
import com.nodeads.test_task.github_client.core.injection.DaggerAppComponent;
import com.nodeads.test_task.github_client.core.injection.Module;
import com.nodeads.test_task.github_client.core.retrofit.enteties.GitUsers;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Igor on 03.08.2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userAvatar;
        TextView userLogin;

        public UserViewHolder(View itemView) {
            super(itemView);
            this.userAvatar = (CircleImageView)itemView.findViewById(R.id.item_user_avatar);
            this.userLogin = (TextView)itemView.findViewById(R.id.item_user_login);
        }

    }

    private List<GitUsers.User> users;
    @Inject AnimationController mAnimationController;

    public UserAdapter(Context context, List<GitUsers.User> users) {
        DaggerAppComponent.builder().module(new Module(context)).build().inject(this);
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.userLogin.setText(users.get(position).getName());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoader.getInstance().displayImage(
                users.get(position).getAvatarUrl(),
                holder.userAvatar,
                options,
                new SimpleImageLoadingListener());

        holder.itemView.setOnClickListener(v -> {
            Intent userIntent = new Intent("com.nodeads.test_task.github_client.USER_ACTIVITY")
                    .putExtra("user_login", users.get(position).getName())
                    .putExtra("user_avatar", users.get(position).getAvatarUrl())
                    .putExtra("user_repository", users.get(position).getReposUrl());
            mAnimationController.transition(holder.userAvatar, "transition_avatar", userIntent);
        });

        holder.itemView.startAnimation(mAnimationController.getAnimation(R.anim.slide_in_top));
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

}
