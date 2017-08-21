package com.boilerplate.view.activity.userlist;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.boilerplate.R;
import com.boilerplate.base.BaseActivity;
import com.boilerplate.constants.AppConstants;
import com.boilerplate.data.User;
import com.boilerplate.util.RecyclerTouchListener;
import com.boilerplate.view.activity.user.UserActivity;
import com.boilerplate.view.adapter.UserListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity implements UserListView {
    private UserListPresenterImpl mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        setPresenter();
    }

    @Override
    protected void setPresenter() {
        super.setPresenter(mPresenter = new UserListPresenterImpl(getComponent(), this));
    }

    @Override
    public void initRecyclerView(List<User> users) {
        UserListAdapter adapter = new UserListAdapter(this, users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        mPresenter.onRecyclerViewItemClick(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        mPresenter.onRecyclerViewItemLongClick(position);
                    }
                }));
    }

    @Override
    public void startUserActivity(User user) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(AppConstants.EXTRA_USER_ID, user.getId());
        startActivity(intent);
    }

    @BindView(R.id.activity_user_list_recycler_view) RecyclerView mRecyclerView;
}