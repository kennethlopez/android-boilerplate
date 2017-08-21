package com.boilerplate.view.fragment.about;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boilerplate.R;
import com.boilerplate.base.BaseFragment;
import com.boilerplate.base.BaseView;
import com.boilerplate.constants.AppConstants;
import com.boilerplate.injection.module.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentAbout extends BaseFragment implements AboutView, BaseView.Click {
    private AboutPresenterImpl mPresenter;

    public static FragmentAbout newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.EXTRA_USER_ID, userId);

        FragmentAbout fragment = new FragmentAbout();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_about, null);
        ButterKnife.bind(this, view);

        setPresenter();
        return view;
    }

    @OnClick(R.id.fragment_about_profile_url)
    @Override
    public void onClick(View view) {
        mPresenter.onClick(view.getId());
    }

    @Override
    protected void setPresenter() {
        super.setPresenter(mPresenter = new AboutPresenterImpl(getComponent(), this));
    }

    @Override
    public void setProfilePic(String url) {
        GlideApp.with(getContext())
                .load(url)
                .thumbnail(0.5f)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_person_black_24dp)
                .into(mProfilePic);
    }

    @Override
    public void setName(int labelResId, String name) {
        String label = getString(labelResId);
        mName.setText(label + " " + name);
    }

    @Override
    public void setProfileUrl(int labelResId, String profileUrl) {
        String label = getString(labelResId);
        mProfileUrl.setText(label + " " + profileUrl);
    }

    @Override
    public void setLocation(int labelResId, String location) {
        String label = getString(labelResId);
        mLocation.setText(label + " " + location);
    }

    @Override
    public void setCompany(int labelResId, String company) {
        String label = getString(labelResId);
        mCompany.setText(label + " " + company);
    }

    @Override
    public void setRepos(int labelResId, String repos) {
        String label = getString(labelResId);
        mRepos.setText(label + " " + repos);
    }

    @Override
    public void setFollowers(int labelResId, String followers) {
        String label = getString(labelResId);
        mFollowers.setText(label + " " + followers);
    }

    @Override
    public void setFollowing(int labelResId, String following) {
        String label = getString(labelResId);
        mFollowing.setText(label + " " + following);
    }

    @Override
    public int getExtraUserId() {
        return getArguments().getInt(AppConstants.EXTRA_USER_ID);
    }

    @Override
    public void openOnBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @BindView(R.id.fragment_about_profile_pic) ImageView mProfilePic;
    @BindView(R.id.fragment_about_name) AppCompatTextView mName;
    @BindView(R.id.fragment_about_profile_url) AppCompatTextView mProfileUrl;
    @BindView(R.id.fragment_about_location) AppCompatTextView mLocation;
    @BindView(R.id.fragment_about_company) AppCompatTextView mCompany;
    @BindView(R.id.fragment_about_repos) AppCompatTextView mRepos;
    @BindView(R.id.fragment_about_followers) AppCompatTextView mFollowers;
    @BindView(R.id.fragment_about_following) AppCompatTextView mFollowing;
}