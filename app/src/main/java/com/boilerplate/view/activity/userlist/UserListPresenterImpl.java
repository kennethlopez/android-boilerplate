package com.boilerplate.view.activity.userlist;



import com.boilerplate.base.BaseJob;
import com.boilerplate.data.User;
import com.boilerplate.data.model.UserModel;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.job.UserBackgroundUpdateJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserListPresenterImpl extends UserListPresenter {
    @Inject
    transient UserModel mUserModel;
    @Inject
    transient JobManager mJobManager;

    private RealmResults<User> mUsers;
    private UserListView mView;

    public UserListPresenterImpl(AppComponent appComponent, UserListView view) {
        super(appComponent, view);
        appComponent.inject(this);

        mUsers = mUserModel.getAllUsers();
        initializeView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserModel.closeRealm();
    }

    @Override
    protected void initializeView(UserListView view) {
        mView = view;
        mView.initRecyclerView(mUsers);
    }

    @Override
    protected void onRecyclerViewItemClick(int position) {
        User user = mUsers.get(position);
        String username = user.getLogin();

        mJobManager.addJobInBackground(new UserBackgroundUpdateJob(BaseJob.BACKGROUND, username));
        mView.startUserActivity(user);
    }

    @Override
    protected void onRecyclerViewItemLongClick(int position) {
    }
}