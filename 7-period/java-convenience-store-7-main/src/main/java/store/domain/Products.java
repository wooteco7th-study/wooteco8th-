package store.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Products {

    private static Map<String, Product> products = new HashMap<>();

    public void put(Product product) {
        products.put(product.getName(), product);
    }

    public Optional<Product> findByName(String name) {
        return Optional.ofNullable(products.get(name));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}
