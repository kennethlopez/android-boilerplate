package com.boilerplate.base;


import io.realm.Realm;
import io.realm.RealmObject;

public abstract class BaseModel {
    private final Realm realm;

    public BaseModel(Realm realm) {
        this.realm = realm;
    }

    public Realm getRealm() {
        return realm;
    }

    public void closeRealm() {
        realm.close();
    }

    public interface OnTransactionCallback<T extends RealmObject> {
        void onRealmSuccess(T data);

        void onRealmError(final Throwable throwable);
    }
}