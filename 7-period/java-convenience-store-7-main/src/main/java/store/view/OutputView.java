package store.view;

import java.util.List;
import store.domain.Product;
import store.dto.freeproduct.FreeProductResult;
import store.dto.freeproduct.FreeProductsResult;
import store.dto.purchasedproduct.PurchasedProductResult;
import store.dto.purchasedproduct.PurchasedProductsResult;

public class OutputView {

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

    public static void showReceipt(PurchasedProductsResult purchasedProductsResult, FreeProductsResult freeProductsResult) {
        System.out.println(NEW_LINE + "===========W 편의점=============");
        showPurchasedProducts(purchasedProductsResult);
        showFreeProducts(freeProductsResult);
    }

    private static void showPurchasedProducts(PurchasedProductsResult purchasedProductsResult) {
        System.out.println("상품명            수량      금액");
        String format = "%s          %d    %,d"; //TODO 포맷 공백 정렬
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
                    freeProductResult.quantity()
            ));
        }
    }
}
