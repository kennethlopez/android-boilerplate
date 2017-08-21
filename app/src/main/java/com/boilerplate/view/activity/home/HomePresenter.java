package com.boilerplate.view.activity.home;


import com.boilerplate.base.BasePresenter;
import com.boilerplate.event.ConnectionErrorEvent;
import com.boilerplate.event.FetchUserEvent;
import com.boilerplate.injection.component.AppComponent;


public abstract class HomePresenter extends BasePresenter<HomeView> implements
        FetchUserEvent.Event, ConnectionErrorEvent.Event {
    public HomePresenter(AppComponent appComponent, HomeView view) {
        super(appComponent, view);
    }

    protected abstract void onTextChangedUsername(CharSequence charSequence, int start,
            int before, int count);
}