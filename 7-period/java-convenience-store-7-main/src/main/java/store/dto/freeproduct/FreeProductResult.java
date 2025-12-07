package store.dto.freeproduct;

public record FreeProductResult(
        String name,
        int totalQuantity,
        int freeProductQuantityByAuto,
        int freeProductQuantityByManual
) {
    public static FreeProductResult from(String name, int freeProductQuantityByAuto, int freeProductQuantityByManual) {
        return new FreeProductResult(
                name,
                freeProductQuantityByAuto + freeProductQuantityByManual,
                freeProductQuantityByAuto,
                freeProductQuantityByManual);
    }
}
