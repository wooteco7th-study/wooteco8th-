package store.domain;

import java.util.Arrays;
import store.exception.ExceptionMessage;

public enum Promotion {

    SODA("탄산2+1"),
    MD_RECOMMEND("MD추천상품"),
    SHINNING_DISCOUNT("반짝할인"),
    NONE("재고 없음");

    private final String name;


    public static Promotion from(String promotionName) {
        return Arrays.stream(Promotion.values())
                .filter(element -> element.name.equals(promotionName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.PROMOTION_NOT_FOUND.getMessage()));
    }

    Promotion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
