package com.boilerplate.view.activity.user;


import android.util.Log;

import com.boilerplate.R;
import com.boilerplate.base.BaseModel;
import com.boilerplate.data.User;
import com.boilerplate.data.model.UserModel;
import com.boilerplate.event.UserBackgroundUpdateEvent;
import com.boilerplate.event.UserUpdatedEvent;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.util.MainUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class UserPresenterImpl extends UserPresenter implements UserView.MenuItemIndexes,
        UserView.FragmentTags, UserBackgroundUpdateEvent.Event {
    @Inject
    transient UserModel mUserModel;

    private static final String TAG = "UserPresenterImpl";

    private UserView mView;
    private User mUser;
    private final String[] mToolbarTitles;
    private String mFragmentTag = "";

    UserPresenterImpl(AppComponent appComponent, UserView view) {
        super(appComponent, view);
        appComponent.inject(this);

        mToolbarTitles = MainUtil.getStringArray(appComponent.context(),
                R.array.user_fragment_toolbar_title);

        initializeView(view);
    }

    @Override
    protected void initializeView(UserView view) {
        mView = view;
        mUser = mUserModel.getUser(mView.getExtraUserId());

        mView.setToolbar();
        mView.initializeNavigationView();
        mView.initializeDrawerHeader();
        mView.setDrawerListener();
        mView.setDrawerHeaderBackground(R.drawable.bg_nav_header, R.mipmap.ic_launcher);

        updateUserFields();
        loadFragment(FRAGMENT_ABOUT);
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
    protected boolean onBackPressed() {
        if (mView.isDrawerOpen()) {
            mView.closeDrawer();
            return false;
        }

        return true;
    }

    @Override
    protected void onClick(int viewId) {
        switch (viewId) {
            case R.id.nav_header_user_activity_profile_url:
                mView.openOnBrowser(mUser.getHtmlUrl());
                break;
        }
    }

    @Override
    protected boolean onNavigationItemSelected(int menuItemId) {
        String fragmentTag;

        switch (menuItemId) {
            default:
            case R.id.menu_user_drawer_about:
                fragmentTag = FRAGMENT_ABOUT;
                break;
            case R.id.menu_user_drawer_repos:
                fragmentTag = FRAGMENT_REPOS;
                break;
            case R.id.menu_user_drawer_followers:
                fragmentTag = FRAGMENT_FOLLOWERS;
                break;
        }

        if (!mFragmentTag.contentEquals(fragmentTag)) {
            mFragmentTag = fragmentTag;
            loadFragment(fragmentTag);
        }

        mView.closeDrawer();

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @Override
    public void onEvent(final UserBackgroundUpdateEvent event) {
        mUserModel.saveAsync(event.getUser(), new BaseModel.OnTransactionCallback<User>() {
            @Override
            public void onRealmSuccess(User data) {
                Log.d(TAG, "onRealmSuccess: ");
                mUser = data;
                updateUserFields();

                // notify fragments of this that the user has been updated
                EventBus.getDefault().post(new UserUpdatedEvent(data));
            }

            @Override
            public void onRealmError(Throwable throwable) {
                Log.d(TAG, "onRealmError: ");
                Log.w(TAG, "onRealmError: ", throwable);
            }
        });

        EventBus.getDefault().removeStickyEvent(UserBackgroundUpdateEvent.class);
    }

    private void updateUserFields() {
        mView.setDrawerHeaderName(mUser.getName());
        mView.setDrawerHeaderProfilePic(mUser.getAvatarUrl(), R.drawable.ic_person_black_24dp);
        mView.setDrawerHeaderProfileUrl(mUser.getHtmlUrl());
    }

    private void loadFragment(String fragmentTag) {
        int index;
        int fragmentId = R.id.app_bar_user_activity_frame;
        int userId = mUser.getId();

        switch (fragmentTag) {
            default:
            case FRAGMENT_ABOUT:
                index = MENU_ITEM_ABOUT_INDEX;
                mView.loadFragmentAbout(fragmentId, fragmentTag, userId);
                break;
            case FRAGMENT_REPOS:
                index = MENU_ITEM_RESPOS_INDEX;
                mView.loadFragmentRepos(fragmentId, fragmentTag, userId);
                break;
            case FRAGMENT_FOLLOWERS:
                index = MENU_ITEM_FOLLOWERS_INDEX;
                mView.loadFragmentFollowers(fragmentId, fragmentTag, userId);
                break;
        }

        mView.selectNavigationMenu(index);
        mView.setToolbarTitle(mToolbarTitles[index]);
    }
}