package com.boilerplate.job;

import android.util.Log;

import com.boilerplate.api.ApiService;
import com.boilerplate.base.BaseJob;
import com.boilerplate.data.Follower;
import com.boilerplate.data.User;
import com.boilerplate.event.FetchUserEvent;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.util.NetworkException;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FetchUserJob extends BaseJob {
    @Inject
    transient Retrofit mRetrofit;

    private static final String TAG = "FetchUserJob";

    private final String mUsername;

    public FetchUserJob(int priority, String username) {
        super(new Params(priority).requireNetwork().persist());
        mUsername = username;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        Response<User> response = mRetrofit.create(ApiService.class)
                .getUser(mUsername)
                .execute();
        if (response.isSuccessful()) {
            User user = response.body();
            Log.d(TAG, "onRun: isSuccessful");

            List<Follower> followers = getFollowers();
            Follower[] followerArray = followers.toArray(new Follower[followers.size()]);
            user.setFollowersList(new RealmList<>(followerArray));

            EventBus.getDefault().postSticky(new FetchUserEvent(user));
        } else {
            String message = response.errorBody().string();
            int code = response.code();
            throwNetworkException(message, code);
        }
    }

    private List<Follower> getFollowers() throws Throwable {
        Response<List<Follower>> response = mRetrofit.create(ApiService.class)
                .getFollowers(mUsername)
                .execute();

        List<Follower> followers = new ArrayList<>();
        if (response.isSuccessful()) {
            Log.d(TAG, "getFollowers: isSuccessful");
            followers = response.body();
        } else {
            throwNetworkException(response.errorBody().string(), response.code());
        }

        return followers;
    }

    private void throwNetworkException(String message, int errorCode) throws NetworkException {
        throw new NetworkException(message, errorCode);
    }
}