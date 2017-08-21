package com.boilerplate.view.fragment.about;


import com.boilerplate.R;
import com.boilerplate.data.User;
import com.boilerplate.data.model.UserModel;
import com.boilerplate.event.UserUpdatedEvent;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.util.MainUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class AboutPresenterImpl extends AboutPresenter implements UserUpdatedEvent.Event {
    @Inject
    transient UserModel mUserModel;

    private AboutView mView;
    private User mUser;

    AboutPresenterImpl(AppComponent appComponent, AboutView view) {
        super(appComponent, view);
        appComponent.inject(this);

        initializeView(view);
    }

    @Override
    protected void initializeView(AboutView view) {
        mView = view;
        mUser = mUserModel.getUser(mView.getExtraUserId());
        updateUserFields();
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
    protected void onClick(int viewId) {
        switch (viewId) {
            case R.id.fragment_about_profile_url:
                mView.openOnBrowser(mUser.getHtmlUrl());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(UserUpdatedEvent event) {
        mUser = event.getUser();
        updateUserFields();
    }

    private void updateUserFields() {
        mView.setProfilePic(mUser.getAvatarUrl());
        mView.setName(R.string.name_colon_text, mUser.getName());
        mView.setProfileUrl(R.string.github_profile_colon_text, mUser.getHtmlUrl());
        mView.setRepos(R.string.repos_colon_text, String.valueOf(mUser.getPublicRepos()));
        mView.setFollowers(R.string.followers_colon_text, String.valueOf(mUser.getFollowers()));
        mView.setFollowing(R.string.following_colon_text, String.valueOf(mUser.getFollowing()));

        String location = mUser.getLocation();
        if (MainUtil.isEmpty(location)) {
            location = "None";
        }
        mView.setLocation(R.string.location_colon_text, location);

        String company = mUser.getCompany();
        if (MainUtil.isEmpty(company)) {
            company = "None";
        }
        mView.setCompany(R.string.company_colon_text, company);
    }
}