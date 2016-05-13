package com.devonulrich.genesisclient.data.files;

import android.content.Context;
import android.content.SharedPreferences;

import com.devonulrich.genesisclient.data.LoginInfo;

public class LoginStore {

    private static final String LOGIN_PREFS_NAME = "com.devonulrich.genesisclient.login";

    private static final String AUTO_LOGIN_KEY = "l9XnDfTMfEXcMfqZ";
    private static final String EMAIL_KEY = "Bt3oeUxjc58qT5qJ";
    private static final String PASS_KEY = "mfaVB1yUcQVMvocS";
    private static final String ID_KEY = "rohfzQNVAP3u4cXq";

    public static LoginInfo getLoginInfo(Context c) {
        SharedPreferences sp = c.getSharedPreferences(LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        LoginInfo li = new LoginInfo();
        li.autoLogin = sp.getBoolean(AUTO_LOGIN_KEY, false);
        li.email = sp.getString(EMAIL_KEY, "");
        li.pass = sp.getString(PASS_KEY, "");
        li.id = sp.getString(ID_KEY, "");

        return li;
    }

    public static void setLoginInfo(LoginInfo li, Context c) {
        SharedPreferences sp = c.getSharedPreferences(LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(AUTO_LOGIN_KEY, li.autoLogin);
        editor.putString(EMAIL_KEY, li.email);
        editor.putString(PASS_KEY, li.pass);
        editor.putString(ID_KEY, li.id);

        editor.apply();
    }

    /*public static void reset(Context c) {
        SharedPreferences sp = c.getSharedPreferences(LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(AUTO_LOGIN_KEY, false);
        editor.putString(EMAIL_KEY, "");
        editor.putString(PASS_KEY, "");
        editor.putString(ID_KEY, "");

        editor.apply();
    }*/
}
