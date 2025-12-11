package store.domain;

import store.dto.request.OrderDecisionRequest;
import store.dto.response.FreeProductResult;
import store.dto.response.PurchasedProductResult;
import store.dto.response.SingleOrderResult;

public class PromotionProcessor {

    public SingleOrderResult process(OrderDecisionRequest request, Order order) {
        int manualFreeQuantity = calculateManualFreeQuantity(order, request);
        minusStock(request, order, manualFreeQuantity);
        return createOrderProcessResult(order, manualFreeQuantity);
    }

    private int calculateManualFreeQuantity(Order order, OrderDecisionRequest request) {
        if (!order.canGetFreeProduct(order.getPromotion())) {
            return 0;
        }
        if (!request.wantExtraFreeProduct()) {
            return 0;
        }
        return order.getPromotionGetQuantity();
    }

    private void minusStock(OrderDecisionRequest request, Order order, int manualFreeQuantity) {
        Inventory inventory = order.getProductInventory();
        int purchasedQuantity = order.getPurchasedQuantity();
        if (manualFreeQuantity > 0) {
            inventory.minusPromotionQuantity(purchasedQuantity + manualFreeQuantity);
            return;
        }
        if (inventory.hasInsufficientPromotionQuantity(purchasedQuantity)) {
            handleInsufficientPromotion(request, order, inventory);
            return;
        }
        inventory.minusPromotionQuantity(purchasedQuantity);
    }

    private void handleInsufficientPromotion(OrderDecisionRequest request, Order order, Inventory inventory) {
        int purchasedQuantity = order.getPurchasedQuantity();
        int insufficientQuantity = order.getInsufficientQuantity();

        if (request.acceptNonPromotionPrice()) {
            inventory.minusPromotionQuantity(purchasedQuantity);
            return;
        }
        inventory.minusPromotionQuantity(purchasedQuantity - insufficientQuantity);
    }

    private SingleOrderResult createOrderProcessResult(Order order, int manualFreeQuantity) {
        FreeProductResult freeProductResult = createFreeProductResult(order, manualFreeQuantity);
        PurchasedProductResult purchasedProductResult = createPurchasedProductResult(order, freeProductResult);

        if (freeProductResult.totalQuantity() == 0) {
            int sumOfNonPromotionAmount = order.getPurchasedQuantity() * order.getProductPrice();
            return SingleOrderResult.createWithoutFreeProduct(purchasedProductResult, sumOfNonPromotionAmount);
        }
        return new SingleOrderResult(purchasedProductResult, freeProductResult, 0);
    }

    private FreeProductResult createFreeProductResult(Order order, int manualFreeQuantity) {
        int autoFreeQuantity = order.calculateFreeProductQuantity();
        return FreeProductResult.from(order.getProduct(), autoFreeQuantity, manualFreeQuantity);
    }

    private PurchasedProductResult createPurchasedProductResult(Order order, FreeProductResult freeProductResult) {
        if (freeProductResult.freeProductQuantityByManual() > 0) {
            return PurchasedProductResult.of(order, order.getPromotionGetQuantity());
        }
        return PurchasedProductResult.from(order);
    }
}
