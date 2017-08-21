package com.boilerplate.base;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.boilerplate.App;
import com.boilerplate.BuildConfig;
import com.boilerplate.R;
import com.boilerplate.constants.NetworkConstants;
import com.boilerplate.event.NetworkExceptionEvent;
import com.boilerplate.injection.component.ActivityComponent;
import com.boilerplate.injection.component.DaggerActivityComponent;
import com.boilerplate.util.NetworkException;
import com.boilerplate.view.activity.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity implements NetworkExceptionEvent.Event,
        NetworkConstants {
    private static final String TAG = "BaseActivity";

    private ActivityComponent mComponent;
    private BasePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(getApp().getAppComponent())
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(NetworkExceptionEvent event) {
        Throwable throwable = event.getThrowable();

        if (throwable instanceof NetworkException) {
            NetworkException exception = (NetworkException) event.getThrowable();

            switch (exception.getErrorCode()) {
                case BAD_REQUEST_400:
                    Log.w(TAG, "onEvent(NetworkExceptionEvent)", exception);
                    showOnServerErrorDialog(getString(R.string.error_400));
                    removeAllStickyEvents();
                    break;
                case UNAUTHORIZED_401:
                    Log.w(TAG, "onEvent(NetworkExceptionEvent)", exception);
                    showOnServerErrorDialog(getString(R.string.error_401));
                    removeAllStickyEvents();
                    break;
                case INTERNAL_SERVER_ERROR_500:
                    Log.w(TAG, "onEvent(NetworkExceptionEvent)", exception);
                    showOnServerErrorDialog(getString(R.string.error_500));
                    removeAllStickyEvents();
                    break;
                case CONNECTION_ERROR_522:
                    showOnConnectionErrorDialog(getString(R.string.error_522));
                    removeAllStickyEvents();
                    break;
                default:
                    String message = "Unhandled network exception occurred";

                    // toast this message only on debug(staging) builds.
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                    Log.e(TAG, message + " " + exception.getErrorCode(), exception);
                    break;
            }
        } else {
            // toast this message only on debug(staging) builds.
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.e(TAG, "unhandled exception", throwable);
        }
    }

    protected abstract void setPresenter();

    public ActivityComponent getComponent() {
        return mComponent;
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }

    protected void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    private void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    private void showOnConnectionErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showOnServerErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.restart_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}