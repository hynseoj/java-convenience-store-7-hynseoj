package store.common.util;

import java.util.Arrays;
import java.util.List;

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
}
