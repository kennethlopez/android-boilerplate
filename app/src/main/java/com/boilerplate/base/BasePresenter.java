package com.boilerplate.base;


import com.boilerplate.injection.component.AppComponent;

public abstract class BasePresenter<T extends BaseView> {
    private T mView;
    private AppComponent mAppComponent;

    public BasePresenter(AppComponent appComponent, T view) {
        mAppComponent = appComponent;
        mView = view;
    }

    protected abstract void initializeView(T view);

    protected void onClick(int vewId) {}

    protected final T getView() {
        return mView;
    }

    protected final AppComponent getComponent() {
        return mAppComponent;
    }

    protected void onStart() {}

    protected void onResume() {}

    protected void onPause() {}

    protected void onStop() {}

    protected void onDestroy() {
        mView = null;
        mAppComponent = null;
    }
}