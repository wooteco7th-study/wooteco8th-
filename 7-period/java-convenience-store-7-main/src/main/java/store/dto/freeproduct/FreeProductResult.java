package store.dto.freeproduct;

public record FreeProductResult(
        String name,
        int quantity
) {
    public static FreeProductResult from(String name, int freeProductQuantity) {
        return new FreeProductResult(name, freeProductQuantity);
    }
}
