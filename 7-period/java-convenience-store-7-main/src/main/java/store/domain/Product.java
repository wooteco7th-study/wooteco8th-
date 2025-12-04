package store.domain;

public class Product {

    private final String name;
    private final int price;
    private final Promotion promotion;
    private final Inventory inventory;

    public Product(String name, int price, Promotion promotion, Inventory inventory) {
        this.name = name;
        this.price = price;
        this.promotion = promotion;
        this.inventory = inventory;
    }

    public void addPromotionQuantity(int quantity) {
        inventory.addPromotionQuantity(quantity);
    }

    public void addNonPromotionQuantity(int quantity) {
        inventory.addNonPromotionQuantity(quantity);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
