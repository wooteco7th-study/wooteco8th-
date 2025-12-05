package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.Order;
import store.domain.Products;
import store.util.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    public void run(Products products) {
        OutputView.showInventory(products.findAll());
        List<Order> orders = createOrders(products);
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
