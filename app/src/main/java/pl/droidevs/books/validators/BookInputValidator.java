package pl.droidevs.books.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookInputValidator {

    private static final String IMAGE_PATTERN = "(http(s?):/)(/[^/]+)+\\.(?:jpg|png)";

    public static boolean isCoverUrlValid(String imageUrl) {
        Matcher matcher = Pattern.compile(IMAGE_PATTERN).matcher(imageUrl);

        return matcher.matches();
    }
}
