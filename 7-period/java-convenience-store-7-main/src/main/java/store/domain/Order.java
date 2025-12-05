package store.domain;

import store.exception.ExceptionMessage;

public class Order {

    private static final int MIN_QUANTITY = 1;

    private final Product product;
    private final int purchasedQuantity;

    public Order(String productName, int purchasedQuantity, Products products) {
        this.product = products.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND.getMessage()));
        validatePurchasedQuantity(purchasedQuantity);
        this.purchasedQuantity = purchasedQuantity;
    }

    private void validatePurchasedQuantity(int purchasedQuantity) {
        if (purchasedQuantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PURCHASED_QUANTITY.getMessage());
        }
    }
}
