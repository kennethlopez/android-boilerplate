package com.boilerplate.view.activity.userlist;


import com.boilerplate.base.BaseView;
import com.boilerplate.data.User;

import java.util.List;

public interface UserListView extends BaseView {
    void initRecyclerView(List<User> users);

    void startUserActivity(User user);
}
