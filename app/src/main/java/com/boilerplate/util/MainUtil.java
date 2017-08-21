package com.boilerplate.util;


import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Pattern;

public final class MainUtil {

    /**
     * Validate github username with regular expression
     * */
    public static boolean isValidUsername(final String username) {
        Pattern pattern = Pattern.compile("^[a-z0-9_-]{3,15}$");
        return pattern.matcher(username).matches();
    }

    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * this method prevents NullPointerException if string is null and returns true
     * */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string);
    }
}