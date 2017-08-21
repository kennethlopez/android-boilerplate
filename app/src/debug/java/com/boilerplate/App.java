package com.boilerplate;


import android.app.Application;

import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.injection.component.DaggerAppComponent;
import com.boilerplate.injection.module.AppModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("boilerplate.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
