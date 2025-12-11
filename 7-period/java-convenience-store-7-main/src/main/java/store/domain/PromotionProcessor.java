package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.dto.request.OrderDecisionRequest;
import store.dto.response.FreeProductResult;
import store.dto.response.OrderProcessResult;
import store.dto.response.PurchasedProductResult;

public class PromotionProcessor {

    public OrderProcessResult process(OrderDecisionRequest request, Order order) {
        int autoFreeQuantity = order.calculateFreeProductQuantity();
        int manualFreeQuantity = calculateManualFreeQuantity(order, request);

        adjustInventory(request, order, manualFreeQuantity);
        FreeProductResult freeProductResult =
                FreeProductResult.from(order.getProduct(), autoFreeQuantity, manualFreeQuantity);
        return buildOrderProcessResult(order, freeProductResult);
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

    private void adjustInventory(OrderDecisionRequest request, Order order, int manualFreeQuantity) {
        Inventory inventory = order.getProduct().getInventory();
        int purchasedQuantity = order.getPurchasedQuantity();

        // 1) 수동 무료 수량에 따른 기본 차감
        if (manualFreeQuantity > 0) {
            int basePromotionQuantity = purchasedQuantity + manualFreeQuantity;
            inventory.minusPromotionQuantity(basePromotionQuantity);
            return;
        }
        // 2) 프로모션 재고 부족 체크 (2-1 단계)
        if (inventory.hasInsufficientPromotionQuantity(purchasedQuantity)) {
            handleInsufficientPromotion(order, request, inventory);
            return;
        }
        inventory.minusPromotionQuantity(purchasedQuantity);
    }

    private void handleInsufficientPromotion(
            Order order,
            OrderDecisionRequest request,
            Inventory inventory
    ) {
        int purchasedQty = order.getPurchasedQuantity();
        int insufficientQty = order.getInsufficientQuantity();

        if (request.acceptNonPromotionPrice()) {
            // 부족분도 정가로 다 사는 경우
            inventory.minusPromotionQuantity(purchasedQty);
            return;
        }

        // 부족한 수량만큼은 안 사고, 프로모션 가능한 수량만 구매
        inventory.minusPromotionQuantity(purchasedQty - insufficientQty);
    }

    private OrderProcessResult buildOrderProcessResult(
            Order order,
            FreeProductResult freeProductResult
    ) {
        List<PurchasedProductResult> purchased = new ArrayList<>();
        List<FreeProductResult> free = new ArrayList<>();
        int sumOfNonPromotionAmount = 0;

        if (freeProductResult.totalQuantity() == 0) {
            purchased.add(PurchasedProductResult.from(order));
            sumOfNonPromotionAmount =
                    order.getPurchasedQuantity() * order.getProduct().getPrice();
            return new OrderProcessResult(purchased, free, sumOfNonPromotionAmount);
        }

        free.add(freeProductResult);
        addPurchasedWithFree(order, freeProductResult, purchased);

        return new OrderProcessResult(purchased, free, sumOfNonPromotionAmount);
    }

    private void addPurchasedWithFree(
            Order order,
            FreeProductResult freeProductResult,
            List<PurchasedProductResult> purchased
    ) {
        if (freeProductResult.freeProductQuantityByManual() > 0) {
            purchased.add(PurchasedProductResult.of(
                    order,
                    order.getPromotion().getGetQuantity()
            ));
            return;
        }
        purchased.add(PurchasedProductResult.from(order));
    }
}
