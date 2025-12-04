package store.domain;

public class Inventory {

    private int promotionQuantity;
    private int nonPromotionQuantity;

    public Inventory(int promotionQuantity, int nonPromotionQuantity) {
        this.promotionQuantity = promotionQuantity;
        this.nonPromotionQuantity = nonPromotionQuantity;
    }

    public void addPromotionQuantity(int quantity) {
        this.promotionQuantity = quantity;
    }

    public void addNonPromotionQuantity(int quantity) {
        this.nonPromotionQuantity = quantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getNonPromotionQuantity() {
        return nonPromotionQuantity;
    }
}
