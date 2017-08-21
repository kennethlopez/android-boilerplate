package com.boilerplate.view.fragment.repos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boilerplate.R;
import com.boilerplate.base.BaseFragment;
import com.boilerplate.constants.AppConstants;

public class FragmentRepos extends BaseFragment {

    public static FragmentRepos newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.EXTRA_USER_ID, userId);

        FragmentRepos fragment = new FragmentRepos();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_repos, null);

        return view;
    }

    @Override
    protected void setPresenter() {

    }
}