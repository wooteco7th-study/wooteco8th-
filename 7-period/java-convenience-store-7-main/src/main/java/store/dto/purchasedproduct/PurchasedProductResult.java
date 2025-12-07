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
                order.getPurchasedQuantity(),
                order.calculatePurchasedPrice(order.getPurchasedQuantity())
        );
    }

    public static PurchasedProductResult of(Order order, int getQuantity) {
        return new PurchasedProductResult(
                order.getProduct().getName(),
                order.getPurchasedQuantity() + getQuantity,
                order.calculatePurchasedPrice(order.getPurchasedQuantity() + getQuantity)
        );
    }
}
