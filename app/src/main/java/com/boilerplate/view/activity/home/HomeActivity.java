package com.boilerplate.view.activity.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.boilerplate.R;
import com.boilerplate.base.BaseActivity;
import com.boilerplate.base.BaseView;
import com.boilerplate.view.activity.userlist.UserListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class HomeActivity extends BaseActivity implements HomeView, BaseView.Click,
        BaseView.TextChanged {
    private HomePresenterImpl mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setPresenter();
    }

    @Override
    public void setPresenter() {
        super.setPresenter(mPresenter = new HomePresenterImpl(getComponent(), this));
    }

    @OnClick(R.id.activity_main_search)
    @Override
    public void onClick(View view) {
        mPresenter.onClick(view.getId());
    }

    @OnTextChanged(
            value = R.id.activity_main_username,
            callback = OnTextChanged.Callback.TEXT_CHANGED
    )
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        mPresenter.onTextChangedUsername(charSequence, start, before, count);
    }

    @Override
    public String getUsername() {
        return mUsername.getText().toString();
    }

    @Override
    public void disableSearch() {
        mSearch.setEnabled(false);
    }

    @Override
    public void enableSearch() {
        mSearch.setEnabled(true);
    }

    @Override
    public void startUserListActivity() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog(int resId) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(resId));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void toastMessage(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    @BindView(R.id.activity_main_search) AppCompatButton mSearch;
    @BindView(R.id.activity_main_username) AppCompatEditText mUsername;
}