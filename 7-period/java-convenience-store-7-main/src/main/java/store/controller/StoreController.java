package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.AnswerCommand;
import store.domain.Order;
import store.domain.Products;
import store.domain.Promotion;
import store.dto.freeproduct.FreeProductResult;
import store.dto.freeproduct.FreeProductsResult;
import store.dto.purchasedproduct.PurchasedProductResult;
import store.dto.purchasedproduct.PurchasedProductsResult;
import store.util.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    public void run(Products products) {
        do {
            purchase(products);
        } while (AnswerCommand.Y.equals(InputView.readAdditionalPurchase()));
    }

    public void purchase(Products products) {
        OutputView.showInventory(products.findAll());
        List<Order> orders = createOrders(products);
        List<FreeProductResult> freeProducts = new ArrayList<>();
        List<PurchasedProductResult> purchasedProducts = new ArrayList<>();
        int sumOfNonPromotionProducts = 0;
        for (Order order : orders) {
            FreeProductResult freeProductResult = process(order);
            if (freeProductResult == null || freeProductResult.totalQuantity() == 0) {
                purchasedProducts.add(PurchasedProductResult.from(order));
                sumOfNonPromotionProducts += order.getPurchasedQuantity() * order.getProduct().getPrice();
                continue;
            }
            freeProducts.add(freeProductResult);
            if (freeProductResult.freeProductQuantityByManual() > 0) {
                purchasedProducts.add(PurchasedProductResult.of(order, order.getPromotion().getGetQuantity()));
                continue;
            }
            purchasedProducts.add(PurchasedProductResult.from(order));
        }
        PurchasedProductsResult purchasedProductsResult = new PurchasedProductsResult(purchasedProducts);
        FreeProductsResult freeProductsResult = new FreeProductsResult(freeProducts);

        int membershipDiscount = 0;
        AnswerCommand answerOfMembership = InputView.readMembership();
        if (answerOfMembership.equals(AnswerCommand.Y)) {
            int discount = sumOfNonPromotionProducts * 30 / 100;
            membershipDiscount = Math.min(8000, discount);
        }
        OutputView.showReceipt(purchasedProductsResult, freeProductsResult, membershipDiscount);
    }

    private FreeProductResult process(Order order) {
        LocalDate orderDate = DateTimes.now().toLocalDate();
        //TODO: 재고 파악 first?
        if (order.isActivePromotion(orderDate)) {
            int freeProductQuantityByAuto = order.calculateFreeProductQuantity();
            Promotion promotion = order.getPromotion();
            int freeProductQuantityByManual = checkFreeProduct(order, promotion);
            checkDiscountPossible(order);
            return FreeProductResult.from(order.getProduct(), freeProductQuantityByAuto, freeProductQuantityByManual);
        }
        order.getProduct().getInventory().minusNonPromotionQuantity(order.getPurchasedQuantity());
        return null;
    }

    private int checkFreeProduct(Order order, Promotion promotion) {
        if (order.canGetFreeProduct(promotion) && isYesOfFreeProduct(order)) {
            return order.getPromotion().getGetQuantity();
        }
        return 0;
    }

    private boolean isYesOfFreeProduct(Order order) {
        AnswerCommand answerCommand = InputView.readAnswerOfFreeProduct(order.getProduct().getName());
        if (answerCommand.equals(AnswerCommand.Y)) {
            order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity() + order.getPromotion().getGetQuantity());
            return true;
        }
        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
        return false;
    }

    private void checkDiscountPossible(Order order) {
        if (!order.getProduct().getInventory().hasInsufficientPromotionQuantity(order.getPurchasedQuantity())) {
            order.getProduct().getInventory().minusPromotionQuantity(order.getPromotion().getBuyQuantity() * order.calculateFreeProductQuantity() + order.calculateFreeProductQuantity());
            return;
        }
        int insufficientQuantity = order.getInsufficientQuantity();
//        int productPromotionQuantity = order.getProductPromotionQuantity();
//        order.getProduct().getInventory().minusPromotionQuantity(productPromotionQuantity);
//        order.getProduct().getInventory().minusNonPromotionQuantity(order.getPurchasedQuantity() - productPromotionQuantity);
        readAnswerOfFullPrice(order, insufficientQuantity);
    }

    private void readAnswerOfFullPrice(Order order, int insufficientQuantity) {
        AnswerCommand answerCommand = InputView.readAnswerOfFullPrice(order.getProduct().getName(), insufficientQuantity);
        if (answerCommand.equals(AnswerCommand.Y)) {
            order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
            return;
        }
        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity() - insufficientQuantity);
    }

    private List<Order> createOrders(Products products) {
        List<Order> orders = new ArrayList<>();
        List<String> productsAndQuantities = Parser.parseByDelimiter(InputView.readProductAndQuantity());
        for (String productAndQuantity : productsAndQuantities) {
            createOrder(products, productAndQuantity, orders);
        }
        return orders;
    }

    private void createOrder(Products products, String productAndQuantity, List<Order> orders) {
        int indexOfDash = productAndQuantity.indexOf("-");
        String productName = productAndQuantity.substring(1, indexOfDash);
        int quantity = Integer.parseInt(productAndQuantity.substring(indexOfDash + 1, productAndQuantity.length() - 1));
        orders.add(new Order(productName, quantity, products));
    }
}
