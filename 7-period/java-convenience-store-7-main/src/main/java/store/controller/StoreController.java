package store.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.AnswerCommand;
import store.domain.Order;
import store.domain.Products;
import store.domain.Promotion;
import store.util.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    public void run(Products products) {
        OutputView.showInventory(products.findAll());
        List<Order> orders = createOrders(products);
        for (Order order : orders) {
            process(order);
        }
        System.out.println();
    }

    private void process(Order order) {
        LocalDate orderDate = LocalDate.now();

        if (order.isActivePromotion(orderDate)) {
            Promotion promotion = order.getPromotion();
            checkFreeProduct(order, promotion);
            return;
        }
        order.getProduct().getInventory().minusNonPromotionQuantity(order.getPurchasedQuantity());
    }

    private void checkFreeProduct(Order order, Promotion promotion) {
        if (order.canGetFreeProduct(promotion)) {
            readAnswerOfFreeProduct(order);
            return;
        }
        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
    }

    private void readAnswerOfFreeProduct(Order order) {
        AnswerCommand answerCommand = InputView.readAnswerOfFreeProduct(order.getProduct().getName());
        if (answerCommand.equals(AnswerCommand.Y)) {
            order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity() + order.getPromotion().getGetQuantity());
            return;
        }
        order.getProduct().getInventory().minusPromotionQuantity(order.getPurchasedQuantity());
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
