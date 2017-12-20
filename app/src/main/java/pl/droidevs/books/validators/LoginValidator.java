package pl.droidevs.books.validators;

import android.text.TextUtils;

public class LoginValidator {
    public static final int LOGIN_MIN_LENGTH = 3;

    public static boolean isLoginValid(String login) {
        return !TextUtils.isEmpty(login) && login.length() >= LOGIN_MIN_LENGTH;
    }
}
