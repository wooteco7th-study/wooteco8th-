package store.dto.purchasedproduct;

import store.domain.Order;

public record PurchasedProductResult(
        String name,
        int quantity,
        int price
) {
    public static PurchasedProductResult from(Order order) {
        return new PurchasedProductResult(
                order.getProduct().getName(),
                order.getPurchasedQuantity(), //FIX free product 포함시켜야 함
                order.calculatePurchasedPrice()
        );
    }
}
