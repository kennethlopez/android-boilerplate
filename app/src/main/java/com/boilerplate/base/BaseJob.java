package com.boilerplate.base;


import android.support.annotation.IntDef;
import android.util.Log;

import com.boilerplate.constants.NetworkConstants;
import com.boilerplate.event.ConnectionErrorEvent;
import com.boilerplate.event.NetworkExceptionEvent;
import com.boilerplate.injection.component.AppComponent;
import com.boilerplate.util.NetworkException;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public abstract class BaseJob extends Job implements NetworkConstants {
    public static final int UI_HIGH = 10;
    public static final int BACKGROUND = 1;
    private static final String TAG = "BaseJob";

    private Throwable mThrowable;
    private boolean mPostOnCancelEvents = true;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UI_HIGH, BACKGROUND})
    public @interface Priority {}

    protected BaseJob(Params params) {
        super(params);
    }

    public void inject(AppComponent appComponent) {

    }

    protected void postOnCancelEvents(boolean postOnCancelEvents) {
        mPostOnCancelEvents = postOnCancelEvents;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
            int maxRunCount) {
        mThrowable = throwable;

        if (throwable instanceof NetworkException) {
            NetworkException exception = (NetworkException) throwable;
            if (exception.shouldRetry()) {
                return RetryConstraint.createExponentialBackoff(runCount, 1000);
            }
        } else if (!mPostOnCancelEvents) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }

        return RetryConstraint.CANCEL;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded: ");
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun: ");
    }

    @Override
    protected void onCancel() {
        Log.d(TAG, "onCancel: ");

        if (mPostOnCancelEvents) {
            // check if exception was caused by device not able to connect to internet
            if (mThrowable instanceof UnknownHostException
                    || mThrowable instanceof SocketTimeoutException) {
                EventBus.getDefault().post(new ConnectionErrorEvent(CONNECTION_ERROR_522));
            } else {
                EventBus.getDefault().post(new NetworkExceptionEvent(mThrowable));
            }
        }
    }

    @Override
    protected int getRetryLimit() {
        // https://github.com/yigit/android-priority-jobqueue/issues/239
        return super.getRetryLimit();
    }
}