package store.dto.purchasedproduct;

import java.util.List;

public record PurchasedProductsResult(
        List<PurchasedProductResult> purchasedProductResults
) {
//    public static PurchasedProductsResult from(List<Order> orders) {
//        List<PurchasedProductResult> purchasedProductResults = orders.stream()
//                .map(order -> PurchasedProductResult.from(order))
//                .toList();
//        return new PurchasedProductsResult(purchasedProductResults);
//    }
}
