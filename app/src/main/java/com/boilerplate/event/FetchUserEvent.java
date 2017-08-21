package com.boilerplate.event;


import com.boilerplate.data.User;

public final class FetchUserEvent {
    private final User user;

    public FetchUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public interface Event {
        @SuppressWarnings("unused")
        void onEvent(FetchUserEvent event);
    }
}
