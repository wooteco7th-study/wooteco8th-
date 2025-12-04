package store;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {

    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";

    public List<String> readLines(String path) {
        try {
            return Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            throw new RuntimeException("파일을 읽는 중 문제가 발생했습니다.", ioException);
        }
    }
}
