package store.util;

import java.util.Arrays;
import java.util.List;

public class Parser {

    private static final String DELIMITER = ",";

    private Parser() {
    }

    public static List<String> parseByDelimiter(String content) {
        return Arrays.stream(content.split(DELIMITER))
                .toList();
    }
}
