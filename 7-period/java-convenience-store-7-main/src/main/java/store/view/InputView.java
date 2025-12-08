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
        String message = "\n현재 " + productName + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
        return readCommand(message);
    }


    public static AnswerCommand readAnswerOfFullPrice(String productName, int insufficientQuantity) {
        String messageFormat = "\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?(Y/N)";
        return readCommand(messageFormat.formatted(productName, insufficientQuantity));
    }

    public static AnswerCommand readMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return AnswerCommand.from(readLine());
    }

    public static AnswerCommand readAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return AnswerCommand.from(readLine());
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
