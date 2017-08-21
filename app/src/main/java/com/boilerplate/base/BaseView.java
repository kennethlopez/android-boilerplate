package com.boilerplate.base;


import android.view.View;

public interface BaseView {
    interface Click {
        void onClick(View view);
    }

    interface TextChanged {
        void onTextChanged(CharSequence charSequence, int start, int before, int count);
    }

    interface OnClickCallback {
        void onClick(View view);
    }
}