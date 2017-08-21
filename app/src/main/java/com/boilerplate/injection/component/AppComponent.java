package com.boilerplate.injection.component;


import android.content.Context;
import android.content.SharedPreferences;

import com.boilerplate.data.model.UserModel;
import com.boilerplate.injection.module.AppModule;
import com.boilerplate.job.UserBackgroundUpdateJob;
import com.boilerplate.job.FetchUserJob;
import com.boilerplate.view.activity.home.HomePresenterImpl;
import com.boilerplate.view.activity.user.UserPresenterImpl;
import com.boilerplate.view.activity.userlist.UserListPresenterImpl;
import com.boilerplate.view.adapter.UserListAdapter;
import com.boilerplate.view.fragment.about.AboutPresenterImpl;
import com.boilerplate.view.viewholder.DrawerHeaderViewHolder;
import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context context();

    SharedPreferences sharedPreferences();

    Gson gson();

    HttpLoggingInterceptor httpLoggingInterceptor();

    Cache cache();

    OkHttpClient okHttpClient();

    Retrofit retrofit();

    JobManager jobManager();

    Realm realm();

    UserModel userModel();

    void inject(FetchUserJob fetchUserJob);

    void inject(HomePresenterImpl homePresenter);

    void inject(UserListPresenterImpl userListPresenter);

    void inject(DrawerHeaderViewHolder drawerHeaderViewHolder);

    void inject(UserPresenterImpl userPresenter);

    void inject(UserListAdapter userListAdapter);

    void inject(AboutPresenterImpl aboutPresenter);

    void inject(UserBackgroundUpdateJob userBackgroundUpdateJob);
}