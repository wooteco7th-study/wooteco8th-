package store.controller;

import java.util.List;
import store.FileReader;
import store.domain.Inventory;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.ProductRepository;
import store.util.Parser;

public class StoreController {

    public void run() {
        ProductRepository productRepository = new ProductRepository();
        List<String> products = FileReader.readProducts();
        for (String productName : products) {
            List<String> productInfo = Parser.parseByDelimiter(productName);
            String name = productInfo.get(0);
            if (productRepository.existsByName(name)) {
                Product product = productRepository.findByName(name);
                if (productInfo.get(3).equals("null")) {
                    product.addNonPromotionQuantity(Integer.parseInt(productInfo.get(2)));
                } else {
                    product.addPromotionQuantity(Integer.parseInt(productInfo.get(2)));
                }
            } else {
                if (productInfo.get(3).equals("null")) {
                    productRepository.save(new Product(name, Integer.parseInt(productInfo.get(1)),
                            Promotion.NONE, new Inventory(0, Integer.parseInt(productInfo.get(2)))));
                } else {
                    productRepository.save(new Product(name, Integer.parseInt(productInfo.get(1)),
                            Promotion.from(productInfo.get(3)), new Inventory(Integer.parseInt(productInfo.get(2)), 0)));
                }
            }
        }
    }
}
