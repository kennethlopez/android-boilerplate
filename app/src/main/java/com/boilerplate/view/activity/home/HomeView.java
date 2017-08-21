package com.boilerplate.view.activity.home;


import com.boilerplate.base.BaseView;

public interface HomeView extends BaseView {

    String getUsername();

    void disableSearch();

    void enableSearch();

    void startUserListActivity();

    void showProgressDialog(int resId);

    void hideProgressDialog();

    void toastMessage(int resId);
}