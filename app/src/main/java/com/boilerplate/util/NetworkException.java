package com.boilerplate.util;


import com.boilerplate.constants.NetworkConstants;
import com.boilerplate.event.NetworkExceptionEvent;

import org.greenrobot.eventbus.EventBus;

public final class NetworkException extends Exception implements NetworkConstants {
    private final String message;
    private final int errorCode;

    public NetworkException(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean shouldRetry() {
        return errorCode < NetworkConstants.BAD_REQUEST_400;
    }
}