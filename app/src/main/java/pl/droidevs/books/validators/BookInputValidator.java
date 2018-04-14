package pl.droidevs.books.validators;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookInputValidator {

    private static final int TITLE_MIN_LENGTH = 3;
    private static final int AUTHOR_MIN_LENGTH = 3;
    private static final int YEAR_MAX_LENGTH = 4;

    public static boolean isCoverUrlValid(String imageUrl) {
        return Patterns.WEB_URL.matcher(imageUrl).matches();
    }

    public static boolean isYearValid(String year) {
        return year.length() <= YEAR_MAX_LENGTH;
    }

    public static boolean isTitleValid(String title) {
        return !TextUtils.isEmpty(title) && title.length() >= TITLE_MIN_LENGTH;
    }

    public static boolean isAuthorValid(String author) {
        return !TextUtils.isEmpty(author) && author.length() >= AUTHOR_MIN_LENGTH;
    }
}
