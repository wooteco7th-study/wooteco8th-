package store.controller;

import store.domain.Products;
import store.view.OutputView;

public class StoreController {

    public void run(Products products) {
        OutputView.showInventory(products.findAll());
    }
}
