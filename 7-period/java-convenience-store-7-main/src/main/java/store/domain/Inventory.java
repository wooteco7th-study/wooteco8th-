package store.domain;

import store.exception.ExceptionMessage;

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

    public void minusPromotionQuantity(int purchasedQuantity) {
        if (promotionQuantity - purchasedQuantity < 0) {
            int purchasedQuantityAfterMinusPromotion = purchasedQuantity - promotionQuantity;
            validateQuantity(nonPromotionQuantity, purchasedQuantityAfterMinusPromotion);
            this.nonPromotionQuantity -= purchasedQuantityAfterMinusPromotion;
            this.promotionQuantity = 0;
            return;
        }
        validateQuantity(promotionQuantity, purchasedQuantity);
        this.promotionQuantity -= purchasedQuantity;
    }

    public void minusNonPromotionQuantity(int purchasedQuantity) {
        validateQuantity(nonPromotionQuantity, purchasedQuantity);
        this.nonPromotionQuantity -= purchasedQuantity;
    }

    private void validateQuantity(int savedQuantity, int purchasedQuantity) {
        if (savedQuantity - purchasedQuantity < 0) {
            throw new IllegalArgumentException(ExceptionMessage.OUT_OF_STOCK.getMessage());
        }
    }
}
