package com.boilerplate.view.fragment.about;


import com.boilerplate.base.BasePresenter;
import com.boilerplate.injection.component.AppComponent;

public abstract class AboutPresenter extends BasePresenter<AboutView> {
    public AboutPresenter(AppComponent appComponent, AboutView view) {
        super(appComponent, view);
    }
}