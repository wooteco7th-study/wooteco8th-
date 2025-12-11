package store.dto.response;

import java.util.List;

public record OrderProcessResult(
        List<PurchasedProductResult> purchasedProducts,
        List<FreeProductResult> freeProducts,
        int sumOfNonPromotionAmount
) {
}
