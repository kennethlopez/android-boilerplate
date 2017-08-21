package com.boilerplate.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.boilerplate.App;
import com.boilerplate.injection.component.ActivityComponent;
import com.boilerplate.injection.component.DaggerActivityComponent;

public abstract class BaseFragment extends Fragment {
    private ActivityComponent mComponent;
    private BasePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = DaggerActivityComponent.builder()
                .appComponent(getApp().getAppComponent())
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    protected abstract void setPresenter();

    public ActivityComponent getComponent() {
        return mComponent;
    }

    protected App getApp() {
        return (App) getActivity().getApplicationContext();
    }

    protected final void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }
}