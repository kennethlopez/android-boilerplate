package com.boilerplate.view.activity.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.boilerplate.R;
import com.boilerplate.base.BaseActivity;
import com.boilerplate.constants.AppConstants;
import com.boilerplate.view.fragment.about.FragmentAbout;
import com.boilerplate.view.fragment.followers.FragmentFollowers;
import com.boilerplate.view.fragment.repos.FragmentRepos;
import com.boilerplate.view.viewholder.DrawerHeaderViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends BaseActivity implements UserView {
    private UserPresenterImpl mPresenter;
    private DrawerHeaderViewHolder mDrawerHeader;
    private Handler mHandler = new Handler();

    private FragmentAbout mFragmentAbout;
    private FragmentRepos mFragmentRepos;
    private FragmentFollowers mFragmentFollowers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        setPresenter();
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void setPresenter() {
        super.setPresenter(mPresenter = new UserPresenterImpl(getComponent(), this));
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public void initializeDrawerHeader() {
        mDrawerHeader = new DrawerHeaderViewHolder(getComponent(),
                mNavigationView.getHeaderView(0),
                new OnClickCallback() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onClick(view.getId());
                    }
                });
    }

    @Override
    public void initializeNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return mPresenter.onNavigationItemSelected(item.getItemId());
                    }
                }
        );
    }

    @Override
    public void setDrawerListener() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.open_drawer_description,
                R.string.close_drawer_description);

        mDrawerLayout.addDrawerListener(drawerToggle);

        // calling sync state is necessary or else your hamburger icon wont show up
        drawerToggle.syncState();
    }

    @Override
    public void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void selectNavigationMenu(int index) {
        mNavigationView.getMenu()
                .getItem(index)
                .setChecked(true);
    }

    @Override
    public void setDrawerHeaderBackground(int resId, int placeHolderResId) {
        mDrawerHeader.setHeaderBackground(resId, placeHolderResId);
    }

    @Override
    public void setDrawerHeaderProfilePic(String url, int placeHolderResId) {
        mDrawerHeader.setProfilePic(url, placeHolderResId);
    }

    @Override
    public void setDrawerHeaderName(String name) {
        mDrawerHeader.setName(name);
    }

    @Override
    public void setDrawerHeaderProfileUrl(String profileUrl) {
        mDrawerHeader.setProfileUrl(profileUrl);
    }

    @Override
    public int getExtraUserId() {
        return getIntent().getIntExtra(AppConstants.EXTRA_USER_ID, -1);
    }

    @Override
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void openOnBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void loadFragmentAbout(int fragmentId, String tag, int userId) {
        if (mFragmentAbout == null) {
            mFragmentAbout = FragmentAbout.newInstance(userId);
        }

        loadFragment(fragmentId, mFragmentAbout, tag);
    }

    @Override
    public void loadFragmentRepos(int fragmentId, String tag, int userId) {
        if (mFragmentRepos == null) {
            mFragmentRepos = FragmentRepos.newInstance(userId);
        }

        loadFragment(fragmentId, mFragmentRepos, tag);
    }

    @Override
    public void loadFragmentFollowers(int fragmentId, String tag, int userId) {
        if (mFragmentFollowers == null) {
            mFragmentFollowers = FragmentFollowers.newInstance(userId);
        }

        loadFragment(fragmentId, mFragmentFollowers, tag);
    }

    private void loadFragment(final int fragmentId, final Fragment fragment, final String tag) {

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(fragmentId, fragment, tag);
                transaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(runnable);
    }

    @BindView(R.id.app_bar_user_activity_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_user_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_user_nav_view) NavigationView mNavigationView;
}