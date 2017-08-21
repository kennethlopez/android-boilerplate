package com.boilerplate.event;


import com.boilerplate.data.User;

public final class UserUpdatedEvent {
    private final User user;

    public UserUpdatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public interface Event {
        void onEvent(final UserUpdatedEvent event);
    }
}