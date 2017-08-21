package com.boilerplate.view.activity.userlist;


import com.boilerplate.base.BasePresenter;
import com.boilerplate.injection.component.AppComponent;

public abstract class UserListPresenter extends BasePresenter<UserListView>{

    public UserListPresenter(AppComponent appComponent, UserListView view) {
        super(appComponent, view);
    }

    protected abstract void onRecyclerViewItemClick(int position);

    protected abstract void onRecyclerViewItemLongClick(int position);
}