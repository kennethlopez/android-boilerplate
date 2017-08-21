package com.boilerplate.job;


import android.util.Log;

import com.boilerplate.api.ApiService;
import com.boilerplate.base.BaseJob;
import com.boilerplate.constants.NetworkConstants;
import com.boilerplate.data.Follower;
import com.boilerplate.data.User;
import com.boilerplate.event.UserBackgroundUpdateEvent;
import com.boilerplate.injection.component.AppComponent;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserBackgroundUpdateJob extends BaseJob {
    @Inject
    transient Retrofit mRetrofit;

    private static final String TAG = "UserBackgroundUpdateJob";

    private final String mUsername;
    private int mGetFollowersResponseCode;

    public UserBackgroundUpdateJob(int priority, String username) {
        super(new Params(priority).requireNetwork().persist());

        // this prevents ConnectionErrorEvent and NetworkExceptionEvent to be posted by EventBus
        // we don't need to handle those events because we only need to update the user object on realm(local db)
        super.postOnCancelEvents(false);
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

            // push event only if the response(user) has been updated
            if (response.code() != NetworkConstants.NOT_MODIFIED_304 && mGetFollowersResponseCode !=
                    NetworkConstants.NOT_MODIFIED_304) {
                EventBus.getDefault().postSticky(new UserBackgroundUpdateEvent(user));
            }
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
            mGetFollowersResponseCode = response.code();
        }

        return followers;
    }
}