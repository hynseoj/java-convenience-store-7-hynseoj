package store.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class StringUtils {

    public static int parseInt(String message) {
        try {
            return Integer.parseInt(message);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    public static List<String> splitWithDelimiter(String message, String delimiter) {
        return Arrays.stream(message.split(delimiter))
                .map(String::strip)
                .toList();
    }

    public static List<String> extractFromRegex(String message, Pattern regex) {
        Matcher matcher = regex.matcher(message);
        return IntStream.range(1, matcher.groupCount() + 1)
                .mapToObj(matcher::group)
                .toList();
    }
}
