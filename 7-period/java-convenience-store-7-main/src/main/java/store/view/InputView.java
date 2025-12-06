package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Pattern;
import store.domain.AnswerCommand;
import store.exception.ExceptionMessage;

public class InputView {

    private static final String NEW_LINE = System.lineSeparator();
    private static final Pattern PRODUCT_QUANTITY_FORMAT = Pattern.compile("^\\[[가-힣]+-\\d+\\](,\\[[가-힣]+-\\d+\\])*$");

    private InputView() {
    }

    public static String readProductAndQuantity() {
        System.out.println(NEW_LINE + "구매하실 상품명과 수량을 입력해 주세요.(예: [사이다-2],[감자칩-1])");
        String input = readLine();
        validateProductAndQuantityFormat(input);
        return input;
    }

    private static void validateProductAndQuantityFormat(String input) {
        if (!PRODUCT_QUANTITY_FORMAT.matcher(input).find()) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_QUANTITY_FORMAT.getMessage());
        }
    }

    public static AnswerCommand readAnswerOfFreeProduct(String productName) {
        String message = "현재 " + productName + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
        return readCommand(message);
    }


    private static AnswerCommand readCommand(String message) {
        System.out.println(message);
        return AnswerCommand.from(readLine());
    }

    private static String readLine() {
        String input = Console.readLine().strip();
        validateInput(input);
        return input;
    }

    private static void validateInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_BLANK.getMessage());
        }
    }
}
