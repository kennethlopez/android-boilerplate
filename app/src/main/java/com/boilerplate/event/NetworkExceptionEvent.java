package com.boilerplate.event;


public final class NetworkExceptionEvent {
    private final Throwable throwable;

    public NetworkExceptionEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public interface Event {
        @SuppressWarnings("unused")
        void onEvent(NetworkExceptionEvent event);
    }
}