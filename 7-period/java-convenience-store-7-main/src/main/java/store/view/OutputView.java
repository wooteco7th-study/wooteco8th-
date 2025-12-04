package store.view;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class OutputView {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String INVENTORY_MESSAGE = """
            안녕하세요. w편의점입니다.
            현재 보유하고 있는 상품입니다.
            
            """;
    private static final String NON_PROMOTION_PRODUCT_FORMAT = "- %s %,d원 %d개";
    private static final String NON_PROMOTION_PRODUCT_OUT_OF_STOCK_FORMAT = "- %s %,d원 재고 없음";
    private static final String PROMOTION_PRODUCT_FORMAT = "- %s %,d원 %d개 %s";
    private static final String PROMOTION_PRODUCT_OUT_OF_STOCK_FORMAT = "- %s %,d원 재고 없음 %s";

    public static void showInventory(List<Product> products) {
        System.out.println(INVENTORY_MESSAGE);
        for (Product product : products) {
            if (product.getPromotion().equals(Promotion.NONE)) {
                int quantity = product.getInventory().getNonPromotionQuantity();
                if (quantity != 0) {
                    System.out.println(NON_PROMOTION_PRODUCT_FORMAT.formatted(product.getName(), product.getPrice(), quantity));
                } else {
                    System.out.println(NON_PROMOTION_PRODUCT_OUT_OF_STOCK_FORMAT.formatted(product.getName(), product.getPrice()));
                }
            } else {
                int promotionQuantity = product.getInventory().getPromotionQuantity();
                if (promotionQuantity != 0) {
                    System.out.println(PROMOTION_PRODUCT_FORMAT.formatted(product.getName(), product.getPrice(), promotionQuantity, product.getPromotion().getName()));
                } else {
                    System.out.println(PROMOTION_PRODUCT_OUT_OF_STOCK_FORMAT.formatted(product.getName(), product.getPrice(), product.getPromotion().getName()));
                }
                int nonPromotionQuantity = product.getInventory().getNonPromotionQuantity();
                if (nonPromotionQuantity != 0) {
                    System.out.println(NON_PROMOTION_PRODUCT_FORMAT.formatted(product.getName(), product.getPrice(), nonPromotionQuantity, product.getPromotion().getName()));
                } else {
                    System.out.println(NON_PROMOTION_PRODUCT_OUT_OF_STOCK_FORMAT.formatted(product.getName(), product.getPrice(), product.getPromotion().getName()));
                }
            }
        }
    }
}
