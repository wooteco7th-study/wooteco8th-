package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.AnswerCommand;
import store.domain.MembershipDiscountPolicy;
import store.domain.Order;
import store.domain.Products;
import store.domain.PromotionProcessor;
import store.dto.request.OrderDecisionRequest;
import store.dto.request.OrderRequest;
import store.dto.response.FreeProductResult;
import store.dto.response.OrderProcessResult;
import store.dto.response.PurchasedProductResult;
import store.util.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    public void run(Products products) {
        do {
            List<Order> orders = makeOrder(products);
            OrderProcessResult orderProcessResult = processOrder(orders);
            int membershipDiscount = processMembershipDiscount(orderProcessResult.sumOfNonPromotionAmount());
            OutputView.showReceipt(orderProcessResult, membershipDiscount);
        } while (wantAdditionalOrder());
    }

    private List<Order> makeOrder(Products products) {
        OutputView.showInventory(products.findAll());
        return RetryHandler.retryOnInvalidInput(() -> createOrders(products));
    }

    private List<Order> createOrders(Products products) {
        List<Order> orders = new ArrayList<>();
        List<OrderRequest> requests = InputView.readProductAndQuantity();
        for (OrderRequest request : requests) {
            Order order = new Order(request.productName(), request.quantity(), products);
            orders.add(order);
        }
        return orders;
    }

    private OrderProcessResult processOrder(List<Order> orders) {
        PromotionProcessor promotionProcessor = new PromotionProcessor();
        List<PurchasedProductResult> purchasedProducts = new ArrayList<>();
        List<FreeProductResult> freeProducts = new ArrayList<>();
        int sumOfNonPromotionAmount = 0;

        for (Order order : orders) {
            OrderProcessResult result = processSingleOrder(order, promotionProcessor);
            purchasedProducts.addAll(result.purchasedProducts());
            freeProducts.addAll(result.freeProducts());
            sumOfNonPromotionAmount += result.sumOfNonPromotionAmount();
        }
        return new OrderProcessResult(purchasedProducts, freeProducts, sumOfNonPromotionAmount);
    }

    private OrderProcessResult processSingleOrder(Order order, PromotionProcessor promotionProcessor) {
        if (order.hasInactivePromotion()) {
            return processInactivePromotion(order);
        }
        OrderDecisionRequest request = askUserDecisions(order);
        return promotionProcessor.process(request, order);
    }

    private OrderProcessResult processInactivePromotion(Order order) {
        PurchasedProductResult purchasedProductResult = PurchasedProductResult.from(order);
        int sumOfNonPromotionAmount = order.calculatePurchasedPrice();
        order.processNonPromotionOrder();
        return new OrderProcessResult(List.of(purchasedProductResult), List.of(), sumOfNonPromotionAmount);
    }

    private OrderDecisionRequest askUserDecisions(Order order) {
        boolean acceptNonPromotionPrice = askAcceptNonPromotionPrice(order);
        boolean wantExtraFreeProduct = askExtraFreeProduct(order);
        return new OrderDecisionRequest(acceptNonPromotionPrice, wantExtraFreeProduct);
    }

    private boolean askAcceptNonPromotionPrice(Order order) {
        if (order.hasSufficientPromotionQuantity()) {
            return false;
        }
        int insufficientQuantity = order.getInsufficientQuantity();
        AnswerCommand answer = RetryHandler.retryOnInvalidInput(
                () -> InputView.readAnswerOfFullPrice(order.getProductName(), insufficientQuantity));
        return answer.isYes();
    }

    private boolean askExtraFreeProduct(Order order) {
        if (order.canGetFreeProduct(order.getPromotion())) {
            AnswerCommand answer = RetryHandler.retryOnInvalidInput(
                    () -> InputView.readAnswerOfFreeProduct(order.getProductName()));
            return answer.isYes();
        }
        return false;
    }

    private int processMembershipDiscount(int sumOfNonPromotionAmounts) {
        MembershipDiscountPolicy membershipDiscountPolicy = new MembershipDiscountPolicy();
        boolean useMembership = RetryHandler.retryOnInvalidInput(InputView::readMembership)
                .isYes();
        return membershipDiscountPolicy.discount(useMembership, sumOfNonPromotionAmounts);
    }

    private boolean wantAdditionalOrder() {
        AnswerCommand answer = RetryHandler.retryOnInvalidInput(InputView::readAdditionalPurchase);
        return answer.isYes();
    }
}

//        for (Order order : orders) {
//            if (order.hasInactivePromotion()) { ㅊㅓ리 완료
//            }
//            FreeProductResult freeProductResult = process(order);
//            if (freeProductResult.totalQuantity() == 0) {
//                purchasedProducts.add(PurchasedProductResult.from(order));
//                sumOfNonPromotionAmount += order.getPurchasedQuantity() * order.getProduct().getPrice();
//                continue;
//            }
//            freeProducts.add(freeProductResult);
//            if (freeProductResult.freeProductQuantityByManual() > 0) {
//                purchasedProducts.add(PurchasedProductResult.of(order, order.getPromotion().getGetQuantity()));
//                continue;
//            }
//            purchasedProducts.add(PurchasedProductResult.from(order));
//        }
//
//        int membershipDiscount = processMembershipDiscount(sumOfNonPromotionAmount);
//        return new OrderProcessResult(purchasedProducts, freeProducts,
//                sumOfNonPromotionAmount, membershipDiscount);


//    private FreeProductResult process(Order order) {
//        int freeProductQuantityByAuto = order.calculateFreeProductQuantity();
//        Promotion promotion = order.getPromotion();
//        int freeProductQuantityByManual = checkFreeProduct(order, promotion);
//        checkDiscountPossible(order);
//        return FreeProductResult.from(order.getProduct(), freeProductQuantityByAuto, freeProductQuantityByManual);
//    }
//
//    private int checkFreeProduct(Order order, Promotion promotion) {
//        if (order.canGetFreeProduct(promotion) && isYesOfFreeProduct(order)) {
//            return order.getPromotion().getGetQuantity();
//        }
//        return 0;
//    }
//
//    private boolean isYesOfFreeProduct(Order order) {
//        AnswerCommand answerCommand = InputView.readAnswerOfFreeProduct(order.getProduct().getName());
//        if (answerCommand.equals(AnswerCommand.Y)) {
//            order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity() + order.getPromotion().getGetQuantity());
//            return true;
//        }
//        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
//        return false;
//    }
//
//    private void checkDiscountPossible(Order order) {
//        if (!order.getProduct().getInventory().hasInsufficientPromotionQuantity(order.getPurchasedQuantity())) {
//            order.getProduct().getInventory().minusPromotionQuantity(order.getPromotion().getBuyQuantity() * order.calculateFreeProductQuantity() + order.calculateFreeProductQuantity());
//            return;
//        }
//        int insufficientQuantity = order.getInsufficientQuantity();
//        readAnswerOfFullPrice(order, insufficientQuantity);
//    }
//
//    private void readAnswerOfFullPrice(Order order, int insufficientQuantity) {
//        AnswerCommand answerCommand = InputView.readAnswerOfFullPrice(order.getProductName(), insufficientQuantity);
//        if (answerCommand.equals(AnswerCommand.Y)) {
//            order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
//            return;
//        }
//        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity() - insufficientQuantity);
//}
