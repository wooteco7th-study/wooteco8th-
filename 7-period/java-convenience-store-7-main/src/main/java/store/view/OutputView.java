package store.view;

import java.util.List;
import store.domain.Product;
import store.dto.freeproduct.FreeProductResult;
import store.dto.freeproduct.FreeProductsResult;
import store.dto.purchasedproduct.PurchasedProductResult;
import store.dto.purchasedproduct.PurchasedProductsResult;

public class OutputView {

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";
    private static final String NEW_LINE = System.lineSeparator();
    private static final String PRODUCT_FORMAT = "- %s %,d원 %s %s";

    public static void showInventory(List<Product> products) {
        System.out.println("""
                안녕하세요. w편의점입니다.
                현재 보유하고 있는 상품입니다.
                """);
        products.forEach(OutputView::showProduct);
    }

    private static void showProduct(Product product) {
        if (product.hasPromotion()) {
            showPromotionProduct(product);
            showNonPromotionProduct(product);
            return;
        }
        showNonPromotionProduct(product);
    }

    private static void showNonPromotionProduct(Product product) {
        int quantity = product.getInventory().getNonPromotionQuantity();
        String quantityFormat = makeQuantityFormat(quantity);
        String nonPromotionFormat = "";
        System.out.println(PRODUCT_FORMAT.formatted(
                product.getName(),
                product.getPrice(),
                quantityFormat,
                nonPromotionFormat
        ));
    }

    private static void showPromotionProduct(Product product) {
        int quantity = product.getInventory().getPromotionQuantity();
        String quantityFormat = makeQuantityFormat(quantity);
        System.out.println(PRODUCT_FORMAT.formatted(
                product.getName(),
                product.getPrice(),
                quantityFormat,
                product.getPromotion().getName()
        ));
    }

    private static String makeQuantityFormat(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    public static void showReceipt(PurchasedProductsResult purchasedProductsResult, FreeProductsResult freeProductsResult, int membershipDiscount) {
        System.out.println(NEW_LINE + "===========W 편의점=============");
        showPurchasedProducts(purchasedProductsResult);
        showFreeProducts(freeProductsResult);

        System.out.println("=============================");
        String format = """
                총구매액            %d     %,d
                행사할인                   -%,d
                멤버십할인                  -%,d
                내실돈                    %,d
                """;
        int totalQuantity = 0;
        int totalPrice = 0;
        for (PurchasedProductResult purchasedProductResult : purchasedProductsResult.purchasedProductResults()) {
            totalQuantity += purchasedProductResult.quantity();
            totalPrice += purchasedProductResult.price();
        }
        int promotionDiscountAmount = 0;
        for (FreeProductResult freeProductResult : freeProductsResult.freeProductsResult()) {
            promotionDiscountAmount += freeProductResult.price();
        }

        System.out.println(format.formatted(
                totalQuantity, totalPrice,
                promotionDiscountAmount,
                membershipDiscount,
                totalPrice - promotionDiscountAmount - membershipDiscount
        ));
    }

    private static void showPurchasedProducts(PurchasedProductsResult purchasedProductsResult) {
        String header = "%-15s %4s %10s%n";
        String format = "%-15s %4d %,10d";

        System.out.printf(header, "상품명", "수량", "금액");
        for (PurchasedProductResult purchasedProductResult : purchasedProductsResult.purchasedProductResults()) {
            System.out.println(format.formatted(
                    purchasedProductResult.name(),
                    purchasedProductResult.quantity(),
                    purchasedProductResult.price()));
        }
    }

    private static void showFreeProducts(FreeProductsResult freeProductsResult) {
        System.out.println("===========증   정=============");
        String format = "%s          %d         ";
        for (FreeProductResult freeProductResult : freeProductsResult.freeProductsResult()) {
            System.out.println(format.formatted(
                    freeProductResult.name(),
                    freeProductResult.totalQuantity()
            ));
        }
    }

    public static void showError(String message) {
        System.out.println(ERROR_MESSAGE_PREFIX + message);
    }
}
