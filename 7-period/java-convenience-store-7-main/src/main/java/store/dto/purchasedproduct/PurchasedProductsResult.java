package store.dto.purchasedproduct;

import java.util.List;
import store.domain.Order;

public record PurchasedProductsResult(
        List<PurchasedProductResult> purchasedProductResults
) {
    public static PurchasedProductsResult from(List<Order> orders) {
        List<PurchasedProductResult> purchasedProductResults = orders.stream()
                .map(PurchasedProductResult::from)
                .toList();
        return new PurchasedProductsResult(purchasedProductResults);
    }
}
