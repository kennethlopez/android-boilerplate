package com.boilerplate.event;


import com.boilerplate.data.User;

public final class UserBackgroundUpdateEvent {
    private final User user;

    public UserBackgroundUpdateEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public interface Event {
        @SuppressWarnings("unused")
        void onEvent(final UserBackgroundUpdateEvent event);
    }
}