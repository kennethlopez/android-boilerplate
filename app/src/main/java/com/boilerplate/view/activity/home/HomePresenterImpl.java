package com.boilerplate.view.activity.home;


import android.util.Log;

import com.boilerplate.R;
import com.boilerplate.base.BaseJob;
import com.boilerplate.base.BaseModel;
import com.boilerplate.data.User;
import com.boilerplate.data.model.UserModel;
import com.boilerplate.event.ConnectionErrorEvent;
import com.boilerplate.event.FetchUserEvent;
import com.boilerplate.event.NetworkExceptionEvent;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.job.FetchUserJob;
import com.boilerplate.util.MainUtil;
import com.boilerplate.util.NetworkException;
import com.path.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class HomePresenterImpl extends HomePresenter {
    @Inject
    transient JobManager mJobManager;
    @Inject
    transient UserModel mUserModel;

    private static final String TAG = "HomePresenterImpl";

    private HomeView mView;

    HomePresenterImpl(AppComponent appComponent, HomeView view) {
        super(appComponent, view);
        appComponent.inject(this);

        initializeView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserModel.closeRealm();
    }

    @Override
    protected void initializeView(HomeView view) {
        mView = view;
        mView.disableSearch();
    }

    @Override
    protected void onClick(int vewId) {
        super.onClick(vewId);
        switch (vewId) {
            case R.id.activity_main_search:
                onClickSearch();
                break;
        }
    }

    @Override
    protected void onTextChangedUsername(CharSequence charSequence, int start,
            int before, int count) {
        if (MainUtil.isValidUsername(String.valueOf(charSequence))) {
            mView.enableSearch();
        } else {
            mView.disableSearch();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @Override
    public void onEvent(FetchUserEvent event) {
        final User user = event.getUser();

        mUserModel.saveAsync(user, new BaseModel.OnTransactionCallback<User>() {
            @Override
            public void onRealmSuccess(User data) {
                Log.d(TAG, "onRealmSuccess: ");
                mView.hideProgressDialog();
                mView.toastMessage(R.string.fetched_user_data_message);
                mView.startUserListActivity();
            }

            @Override
            public void onRealmError(Throwable throwable) {
                Log.d(TAG, "onRealmError: ");
                Log.w(TAG, "onRealmError: ", throwable);
                mView.hideProgressDialog();
                mView.toastMessage(R.string.problem_saving_user_error_message);
            }
        });

        EventBus.getDefault().removeStickyEvent(FetchUserEvent.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(ConnectionErrorEvent event) {
        mView.hideProgressDialog();

        // notifies the BaseActivity that the phone cannot connect to the internet
        NetworkException exception = new NetworkException("internet connection error",
                event.getErrorCode());

        EventBus.getDefault().post(new NetworkExceptionEvent(exception));
    }

    private void onClickSearch() {
        String username = mView.getUsername();

        if (!mUserModel.hasUser(username)) {
            mView.showProgressDialog(R.string.fetching_user_data_text);
            mJobManager.addJobInBackground(new FetchUserJob(BaseJob.UI_HIGH, username));
        } else {
            mView.startUserListActivity();
        }
    }
}