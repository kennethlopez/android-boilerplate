package com.boilerplate.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boilerplate.R;
import com.boilerplate.data.User;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.view.viewholder.UserListItemViewHolder;

import java.util.List;

import javax.inject.Inject;

public class UserListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {
    private final Context mContext;
    private final List<User> mUsers;

    public UserListAdapter(Context context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    @Override
    public UserListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_user, parent, false);
        return new UserListItemViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(UserListItemViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.setName(user.getName());
        holder.setProfilePic(user.getAvatarUrl());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}