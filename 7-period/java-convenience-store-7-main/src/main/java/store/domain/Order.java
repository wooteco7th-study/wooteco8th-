package store.domain;

import java.time.LocalDate;
import store.exception.ExceptionMessage;

public class Order {

    private static final int MIN_QUANTITY = 1;

    private final Product product;
    private final int purchasedQuantity;

    public Order(String productName, int purchasedQuantity, Products products) {
        this.product = products.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND.getMessage()));
        validatePurchasedQuantity(purchasedQuantity);
        this.purchasedQuantity = purchasedQuantity;
    }

    private void validatePurchasedQuantity(int purchasedQuantity) {
        if (purchasedQuantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PURCHASED_QUANTITY.getMessage());
        }
    }

    public boolean isActivePromotion(LocalDate orderDate) {
        Promotion promotion = product.getPromotion();
        return promotion.isActive(orderDate);
    }

    public boolean canGetFreeProduct(Promotion promotion) {
        return promotion.canGetFreeProduct(purchasedQuantity, getProductPromotionQuantity());
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        return product.getPromotion();
    }

    public int getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public int getProductPromotionQuantity() {
        return product.getInventory().getPromotionQuantity();
    }

    public int getInsufficientQuantity() {
        int totalQuantity = getPromotion().getBuyQuantity() + getPromotion().getGetQuantity();
        return purchasedQuantity - (totalQuantity * (getProductPromotionQuantity() / totalQuantity));
    }

    public int calculatePurchasedPrice(int purchasedQuantity) {
        return product.getPrice() * purchasedQuantity;
    }

    public int calculateFreeProductQuantity() {
        int promotionQuantity = Math.min(product.getInventory().getPromotionQuantity(), purchasedQuantity);
        return promotionQuantity / (getPromotion().getBuyQuantity() + getPromotion().getGetQuantity());
    }

    public boolean hasInsufficientQuantity() {
        if (purchasedQuantity > getProductPromotionQuantity() + product.getInventory().getNonPromotionQuantity()) {
            return true;
        }
        return false;
    }
}
