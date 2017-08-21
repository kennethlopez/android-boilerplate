package com.boilerplate.data.model;


import com.boilerplate.base.BaseModel;
import com.boilerplate.data.User;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserModel extends BaseModel {

    public UserModel(Realm realm) {
        super(realm);
    }

    public RealmResults<User> getAllUsers() {
        return getRealm().where(User.class).findAll();
    }

    public User getUser(int userId) {
        return getRealm().where(User.class)
                .equalTo("id", userId)
                .findFirst();
    }

    public void saveAsync(final User user, final OnTransactionCallback<User> callback) {
        getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onRealmSuccess(user);
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            }
        });
    }

    public boolean hasUser(String username) {
        User user = getRealm().where(User.class)
                .equalTo("login", username, Case.INSENSITIVE)
                .not()
                .findFirst();
        return user != null;
    }
}