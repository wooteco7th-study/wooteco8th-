package store.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;

public class ProductRepository {

    private static Map<String, Product> products = new HashMap<>();

    public void save(Product product) {
        products.put(product.getName(), product);
    }

    public boolean existsByName(String name) {
        return products.containsKey(name);
    }

    public Product findByName(String name) {
        return products.get(name);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}
