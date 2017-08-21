package com.boilerplate.event;


public final class ConnectionErrorEvent {
    private final int errorCode;

    public ConnectionErrorEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public interface Event {
        @SuppressWarnings("unused")
        void onEvent(ConnectionErrorEvent event);
    }
}