package pl.droidevs.books.validators;

import android.text.TextUtils;

public class PasswordValidator {
    public static final int MIN_LENGTH = 6;

    public static boolean isValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_LENGTH;
    }
}
