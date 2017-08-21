package com.boilerplate.view.activity.user;


import com.boilerplate.base.BasePresenter;
import com.boilerplate.injection.component.AppComponent;

public abstract class UserPresenter extends BasePresenter<UserView> {
    public UserPresenter(AppComponent appComponent, UserView view) {
        super(appComponent, view);
    }

    protected abstract boolean onBackPressed();

    protected abstract boolean onNavigationItemSelected(int menuItemId);
}
